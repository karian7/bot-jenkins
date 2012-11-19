/**
 * 
 */
package com.daumcorp.commdev1.bot.ci.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daumcorp.commdev1.bot.ci.BotMessageReceiver;
import com.daumcorp.commdev1.bot.ci.CIInterface;
import com.daumcorp.commdev1.bot.ci.CIMessageReceiver;
import com.daumcorp.commdev1.bot.ci.GroupEventListener;
import com.daumcorp.commdev1.bot.ci.Storable;
import com.daumcorp.commdev1.bot.ci.spring.StorableHandler;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * @author karian7
 * 
 */
@Component
public class BotMessageReceiverImpl implements BotMessageReceiver, Storable {
	private final ConcurrentMap<String, GroupEventListener> listeners = Maps.newConcurrentMap();
	@Autowired
	private CIMessageReceiver ciFailController;
	@Autowired
	private CIInterface ciInterface;

	@Override
	public void newGroup(String groupId) {
		addCI(groupId, null);
	}

	@Override
	public GroupEventListener kick(String groupId) {
		removeCI(groupId, null); // removes all ci listening
		GroupEventListener remove = listeners.remove(groupId);
		remove.kick();
		return remove;
	}

	@Override
	public void addCI(String groupId, String ciJobId) {
		Group group = new Group();
		group.setId(groupId);
		if (ciJobId != null && !ciInterface.existsCI(ciJobId)) {
			group.notFoundCI(ciJobId);
			return;
		}
		listeners.putIfAbsent(groupId, group);
		ciFailController.addListener(listeners.get(groupId), ciJobId);
	}

	@Override
	public void removeCI(String groupId, String ciJobId) {
		Group group = new Group();
		group.setId(groupId);
		if (ciJobId != null && !ciInterface.existsCI(ciJobId)) {
			group.notFoundCI(ciJobId);
			return;
		}
		listeners.putIfAbsent(groupId, group);
		ciFailController.removeListener(listeners.get(groupId), ciJobId);
	}

	@Override
	public String store() {
		Map<String, String> serialized = Maps.newHashMap();
		for (Entry<String, GroupEventListener> entity : listeners.entrySet()) {
			serialized.put(entity.getKey(), StorableHandler.serializeObject(entity.getValue()));
		}
		return new Gson().toJson(serialized);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load(String json) {
		Map<String, String> loaded = new Gson().fromJson(json, HashMap.class);
		Map<String, GroupEventListener> deserialized = Maps.newHashMap();
		for (Entry<String, String> entry : loaded.entrySet()) {
			deserialized.put(entry.getKey(), StorableHandler.<GroupEventListener> deserializeObject(entry.getValue()));
		}

		listeners.putAll(deserialized);
	}

}
