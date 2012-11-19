package com.daumcorp.commdev1.bot.ci.spring;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.daumcorp.commdev1.bot.ci.Storable;
import com.daumcorp.commdev1.bot.ci.spring.StorableHandler;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

import foo.bar.TestBean;

@RunWith(MockitoJUnitRunner.class)
public class StorableHandlerTest {
	private static final String STORED = "{\"b\":\"bbb\",\"a\":\"aaa\"}";
	private static final String TEST_FILE = System.getProperty("user.home") + "/.cafe-bot-ci/foo/bar/TestBean.store";
	private TestBean testBean = new TestBean();
	@Mock
	private ApplicationContext applicationContext;
	@InjectMocks
	private StorableHandler storableHandler = new StorableHandler();

	@Test
	public void storeAndLoad() throws Exception {
		new File(TEST_FILE).delete();
		testBean.map.put("a", "aaa");
		testBean.map.put("b", "bbb");
		when(applicationContext.getBeansOfType(Storable.class)).thenReturn(ImmutableMap.<String, Storable> of("testBean", testBean));

		storableHandler.destroy();

		assertThat(Files.toString(new File(TEST_FILE), Charsets.UTF_8).trim(), is(STORED));

		storableHandler.afterPropertiesSet();

		assertThat(testBean.map, allOf(hasEntry("a", "aaa"), hasEntry("b", "bbb")));
	}
}
