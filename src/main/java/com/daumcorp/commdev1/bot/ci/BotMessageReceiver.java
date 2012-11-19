package com.daumcorp.commdev1.bot.ci;

public interface BotMessageReceiver {
	void newGroup(String groupId);

	/**
	 * @param string
	 */
	GroupEventListener kick(String groupId);

	/**
	 * @param string
	 * @param string2
	 */
	void addCI(String groupId, String ciJobId);

	/**
	 * @param string
	 * @param string2
	 */
	void removeCI(String groupId, String ciJobId);

}
