package com.daumcorp.commdev1.bot.ci.impl;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.daumcorp.commdev1.bot.ci.BotCommand;
import com.daumcorp.commdev1.bot.ci.BotInterface;
import com.daumcorp.commdev1.bot.ci.GroupEventListener;
import com.daumcorp.commdev1.bot.ci.dto.CIResult;
import com.daumcorp.commdev1.bot.ci.dto.Member;
import com.daumcorp.commdev1.bot.ci.spring.ApplicationContextProvider;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;

public class Group implements GroupEventListener, Serializable {

	private static final long serialVersionUID = 8122887288815195785L;

	@Expose
	private ConcurrentMap<String, Member> members = Maps.newConcurrentMap();
	@Expose
	private String id;

	@Override
	public void kick() {
		getBotInterface().exitGroup(getId());
	}

	@Override
	public void onFail(CIResult result) {
		for (String memberId : result.getParticipants()) {
			Member old = members.putIfAbsent(memberId, new Member(memberId));
			(old != null ? old : members.get(memberId)).decScore();
		}
		String msgFail = result.getJobId() + " 프로젝트가 Unstable 됨. 자세한 내용은 " + result.getUrl();
		Set<String> participants = Sets.newHashSet(result.getParticipants());
		if (!participants.isEmpty()) {
			msgFail += "\n" + Joiner.on(',').join(participants) + " 님, 커피사세요.";
		}
		getBotInterface().sendGroupMessage(id, msgFail);
	}

	@Override
	public void onSuccess(CIResult result) {
		for (String memberId : result.getParticipants()) {
			Member old = members.putIfAbsent(memberId, new Member(memberId));
			(old != null ? old : members.get(memberId)).incScore();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ConcurrentMap<String, Member> getMembers() {
		return members;
	}

	public void setMembers(ConcurrentMap<String, Member> members) {
		this.members = members;
	}

	public BotInterface getBotInterface() {
		return ApplicationContextProvider.getApplicationContext().getBean(BotInterface.class);
	}

	@Override
	public void notFoundCI(String ciJobId) {
		getBotInterface().sendGroupMessage(getId(), ciJobId + " 프로젝트는 http://ci.daumcorp.com 에 등록되어 있지 않습니다.");
	}

	@Override
	public void startListeningCI(String ciJobId) {
		getBotInterface().sendGroupMessage(getId(), ciJobId + " 프로젝트 모니터링 시작합니다.");
	}

	@Override
	public void stopListeningCI(String ciJobId) {
		getBotInterface().sendGroupMessage(getId(), ciJobId + "에 대해 더이상 알리지 않습니다.");
	}

	@Override
	public void welcome() {
		getBotInterface().sendGroupMessage(getId(), BotCommand.help());
	}
}
