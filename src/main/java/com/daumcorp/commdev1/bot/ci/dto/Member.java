package com.daumcorp.commdev1.bot.ci.dto;

import java.io.Serializable;

public class Member implements Serializable {
	private static final long serialVersionUID = -8574945777232417056L;
	
	private String id;
	private int score;

	public Member(String author) {
		this.id = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
		Member other = (Member) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public void incScore() {
		score++;
	}

	public void decScore() {
		score -= 5;
	}
}
