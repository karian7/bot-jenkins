package com.daumcorp.commdev1.bot.ci;

public interface Storable {
	String store();
	void load(String json);
}
