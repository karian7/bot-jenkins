package com.daumcorp.commdev1.bot.ci.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class CIResult {
	private boolean success;
	private List<String> participants = Lists.newArrayList();
	private String jobId;
	private String url;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}
}
