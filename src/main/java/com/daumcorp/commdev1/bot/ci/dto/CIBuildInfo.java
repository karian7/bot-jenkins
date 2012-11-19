package com.daumcorp.commdev1.bot.ci.dto;

public class CIBuildInfo {
	private int number;
	private String phase;
	private String status;
	private String full_url;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFull_url() {
		return full_url;
	}

	public void setFull_url(String fullUrl) {
		this.full_url = fullUrl;
	}

}
