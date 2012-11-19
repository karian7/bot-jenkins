package com.daumcorp.commdev1.bot.ci.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


import com.daumcorp.commdev1.bot.ci.dto.CIResult;
import com.daumcorp.commdev1.bot.ci.impl.CIInterfaceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CIInterfaceImplTest {

	@Test
	public void parse() throws IOException {
		CIInterfaceImpl ciInterface = new CIInterfaceImpl();
		CIResult result = ciInterface.parseBuild(getClass().getResourceAsStream("ci_find_858.json"));

		assertNotNull(result);
		assertThat(result.isSuccess(), is(false));
		assertThat(result.getJobId(), is("cafe-web"));
		assertThat(result.getUrl(), is("http://ci.daumcorp.com/job/cafe-web/858/"));
		assertThat(result.getParticipants(), hasItems("hk87", "karian7"));
	}
	
	@Test
	public void parse2() throws IOException {
		CIInterfaceImpl ciInterface = new CIInterfaceImpl();
		CIResult result = ciInterface.parseBuild(getClass().getResourceAsStream("ci_find_cafe-node-mapinf_7.json"));
		
		assertNotNull(result);
		assertThat(result.isSuccess(), is(false));
		assertThat(result.getJobId(), is("cafe-node-mapinfo"));
		assertThat(result.getUrl(), is("http://ci.daumcorp.com/job/cafe-node-mapinfo/7/"));
		assertThat(result.getParticipants(), hasItems("adeldel"));
	}
}
