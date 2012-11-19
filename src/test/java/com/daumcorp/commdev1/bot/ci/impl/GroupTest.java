package com.daumcorp.commdev1.bot.ci.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.context.ApplicationContext;

import com.daumcorp.commdev1.bot.ci.BotInterface;
import com.daumcorp.commdev1.bot.ci.dto.CIResult;
import com.daumcorp.commdev1.bot.ci.dto.Member;
import com.daumcorp.commdev1.bot.ci.spring.ApplicationContextProvider;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class GroupTest {
	@Mock
	private BotInterface botInterface;

	private final Group group = new Group();

	@Before
	public void setUp() {
		group.setId("GRPID");
		ApplicationContext mock = mock(ApplicationContext.class);
		when(mock.getBean(BotInterface.class)).thenReturn(botInterface);
		new ApplicationContextProvider().setApplicationContext(mock);
	}

	@Test
	public void onSuccess() {
		assertThat(members().size(), is(0));

		CIResult ciResult = new CIResult();
		ciResult.setParticipants(Lists.newArrayList("karian7"));
		group.onSuccess(ciResult);

		assertThat(members().size(), is(1));

		assertThat(members().get("karian7").getScore(), is(1));
	}

	@Test
	public void onFail() {
		assertThat(members().size(), is(0));

		CIResult ciResult = new CIResult();
		ciResult.setParticipants(Lists.newArrayList("karian7"));
		ciResult.setJobId("cafe-web");
		ciResult.setUrl("http://ci.daumcorp.com/job/cafe-web/5");
		group.onFail(ciResult);

		assertThat(members().size(), is(1));

		assertThat(members().get("karian7").getScore(), is(-5));

		verify(botInterface).sendGroupMessage("GRPID", "cafe-web 프로젝트가 Unstable 됨. 자세한 내용은 http://ci.daumcorp.com/job/cafe-web/5\nkarian7 님, 커피사세요.");
	}

	@SuppressWarnings("unchecked")
	private Map<String, Member> members() {
		return Whitebox.getInternalState(group, Map.class);
	}

	@Test
	public void testEquals()
	 throws Exception {
	
	}
}
