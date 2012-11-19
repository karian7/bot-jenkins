package com.daumcorp.commdev1.bot.ci;

import com.daumcorp.commdev1.bot.ci.dto.CIResult;

public interface CIListener {
	void notFoundCI(String ciJobId);

	void startListeningCI(String ciJobId);
	
	void stopListeningCI(String ciJobId);

	void onFail(CIResult result);

	void onSuccess(CIResult result);

	void welcome();

}
