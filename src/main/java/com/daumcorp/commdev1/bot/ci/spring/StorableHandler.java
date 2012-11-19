package com.daumcorp.commdev1.bot.ci.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.daumcorp.commdev1.bot.ci.Storable;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Component
public class StorableHandler implements InitializingBean, DisposableBean, ApplicationContextAware {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String LOCALSTORE_BASEDIR = System.getProperty("user.home") + "/.cafe-bot-ci/";
	private ApplicationContext applicationContext;
	private ScheduledExecutorService service;

	@Override
	public void destroy() throws Exception {
		store();
	}

	private void store() throws IOException {
		Map<String, Storable> map = applicationContext.getBeansOfType(Storable.class);
		for (Storable storable : map.values()) {
			File parentDir = new File(LOCALSTORE_BASEDIR + packageToDir(storable));
			File to = new File(parentDir, storable.getClass().getSimpleName() + ".store");
			Files.createParentDirs(to);
			Files.touch(to);
			String store = storable.store();
			logger.info("store: {}", store);
			Files.write(store, to, Charsets.UTF_8);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Storable> map = applicationContext.getBeansOfType(Storable.class);
		for (Storable storable : map.values()) {
			File parentDir = new File(LOCALSTORE_BASEDIR + packageToDir(storable));
			File to = new File(parentDir, storable.getClass().getSimpleName() + ".store");
			if (to.exists()) {
				String load = Files.toString(to, Charsets.UTF_8);
				logger.info("load: {}", load);
				storable.load(load);
			}
		}
		
		service = Executors.newScheduledThreadPool(2, new ThreadFactoryBuilder().setDaemon(true).setNameFormat("StorableAutoSave-%d").build());
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("Autosave by scheduler..");
					store();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}, 1, 1, TimeUnit.HOURS);
	}

	private String packageToDir(Storable storable) {
		return StringUtils.replace(storable.getClass().getPackage().getName(), ".", "/");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static String serializeObject(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			new ObjectOutputStream(baos).writeObject(o);
			return new String(baos.toByteArray(), Charsets.ISO_8859_1);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserializeObject(String serialized) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serialized.getBytes(Charsets.ISO_8859_1)));
			return (T) ois.readObject();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		} catch (ClassNotFoundException e) {
			throw Throwables.propagate(e);
		}

	}
}
