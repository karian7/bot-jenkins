/**
 * 
 */
package com.daumcorp.commdev1.bot.ci;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author karian7
 * 
 */
public enum BotCommand {
	kick("CI봇 이제그만"), addCI("CI봇 알려줘"), removeCI("CI봇 듣기싫어"), helpCI("CI봇 /?"), nullCmd("이해불가");

	public final String message;

	BotCommand(String message) {
		this.message = message;
		BotMessages.messages.put(message, this);
	}

	public static BotCommand find(String message) {
		for (Entry<String, BotCommand> entry : BotMessages.messages.entrySet()) {
			if (StringUtils.contains(message, entry.getKey())) {
				return entry.getValue();
			}
		}
		return nullCmd;
	}

	public static String help() {
		return Joiner.on("\n").join(Lists.newArrayList("http://ci.daumcorp.com 프로젝트 unstable 알림봇.", addCI.message + " [id]:", "  - id 프로젝트 모니터링", removeCI.message + " [id]:", "  - id 프로젝트 모니터링 중지", 
				kick.message + ":", "  - CI봇 퇴장", helpCI.message + ":", "  - 명령어 목록"));
	}

	static class BotMessages {
		public static Map<String, BotCommand> messages = Maps.newHashMap();
	}
}
