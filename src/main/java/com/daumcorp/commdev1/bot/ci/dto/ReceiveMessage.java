/**
 * 
 */
package com.daumcorp.commdev1.bot.ci.dto;

import org.apache.commons.lang.StringUtils;


/**
 * @author karian7
 * 
 */
public class ReceiveMessage {
	private String BOT_ID = "PU_wr1moP9zMi50";
	private String action;
	private String groupId;
	private String buddyId;
	private String content;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the buddyId
	 */
	public String getBuddyId() {
		return buddyId;
	}

	/**
	 * @param buddyId the buddyId to set
	 */
	public void setBuddyId(String buddyId) {
		this.buddyId = buddyId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCIBot() {
		return StringUtils.equals(BOT_ID, StringUtils.substringBetween(content, "\"buddyId\":\"", "\""));
	}
}
