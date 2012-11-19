package com.daumcorp.commdev1.bot.ci;

public interface BotInterface {

	void exitGroup(String groupId);

	void sendGroupMessage(String groupId, String msg);

}
