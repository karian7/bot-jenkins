package com.daumcorp.commdev1.bot.ci;

import com.daumcorp.commdev1.bot.ci.dto.CIResult;

public interface CIInterface {
	CIResult findBuild(String url);
	boolean existsCI(String ciJobId);
}
