package com.daumcorp.commdev1.bot.ci.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daumcorp.commdev1.bot.ci.CIInterface;
import com.daumcorp.commdev1.bot.ci.CIListener;
import com.daumcorp.commdev1.bot.ci.CIMessageReceiver;
import com.daumcorp.commdev1.bot.ci.Storable;
import com.daumcorp.commdev1.bot.ci.dto.CIBuildInfo;
import com.daumcorp.commdev1.bot.ci.dto.CIResult;
import com.daumcorp.commdev1.bot.ci.spring.StorableHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Controller
@RequestMapping("/ci")
public class CIController implements CIMessageReceiver, Storable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ConcurrentMap<CIListener, Set<String>> listeners = Maps.newConcurrentMap();
	@Autowired
	private CIInterface ciInterface;

	@RequestMapping(value = "/noti")
	@ResponseBody
	public void receiveBuild(HttpServletRequest req) throws IOException {
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader(req.getInputStream()));
		logger.info("receive build: {}", jsonElement);

		String ciJobId = jsonElement.getAsJsonObject().get("name").getAsString();
		CIBuildInfo ciBuildInfo = new Gson().fromJson(jsonElement.getAsJsonObject().get("build"), CIBuildInfo.class);
		if ("FINISHED".equals(ciBuildInfo.getPhase())) {
			notify(ciJobId, ciBuildInfo.getFull_url());
		}
	}

	@RequestMapping(value = "/noti_test")
	public ModelAndView testView(HttpServletRequest req) throws IOException {
		return new ModelAndView("noti_test");
	}

	@Override
	public void removeListener(CIListener ciFailListener, String ciJobId) {
		if (ciJobId == null) {
			listeners.remove(ciFailListener);
		} else {
			listeners.get(ciFailListener).remove(ciJobId);
			ciFailListener.stopListeningCI(ciJobId);
		}
	}

	@Override
	public void addListener(CIListener ciFailListener, String ciJobId) {
		listeners.putIfAbsent(ciFailListener, Sets.<String> newHashSet());
		if (ciJobId != null) {
			listeners.get(ciFailListener).add(ciJobId);
			ciFailListener.startListeningCI(ciJobId);
		} else {
			ciFailListener.welcome();
		}
	}

	void notify(String ciJobId, String url) {
		for (Entry<CIListener, Set<String>> entry : listeners.entrySet()) {
			for (String listenCiJobId : entry.getValue()) {
				if (StringUtils.equals(ciJobId, listenCiJobId)) {
					CIResult result = ciInterface.findBuild(url);
					if (result.isSuccess()) {
						entry.getKey().onSuccess(result);
					} else {
						entry.getKey().onFail(result);
					}
				}
			}
		}
	}

	@Override
	public String store() {
		Map<String, Set<String>> serialized = Maps.newHashMap();
		for (Entry<CIListener, Set<String>> entity : listeners.entrySet()) {
			serialized.put(StorableHandler.serializeObject(entity.getKey()), entity.getValue());
		}
		return new Gson().toJson(serialized);
	}

	@Override
	public void load(String json) {
		@SuppressWarnings("unchecked")
		Map<String, List<String>> loaded = new Gson().fromJson(json, HashMap.class);
		Map<CIListener, Set<String>> deserialized = Maps.newHashMap();
		for (Entry<String, List<String>> entity : loaded.entrySet()) {
			deserialized.put(StorableHandler.<CIListener> deserializeObject(entity.getKey()), Sets.newHashSet(entity.getValue()));
		}

		listeners.putAll(deserialized);
	}

}