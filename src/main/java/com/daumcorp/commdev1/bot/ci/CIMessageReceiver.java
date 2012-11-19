package com.daumcorp.commdev1.bot.ci;

public interface CIMessageReceiver {
	void removeListener(CIListener ciFailListener, String ciJobId);

	void addListener(CIListener ciFailListener, String ciJobId);
}
