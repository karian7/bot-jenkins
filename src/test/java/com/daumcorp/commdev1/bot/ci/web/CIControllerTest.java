package com.daumcorp.commdev1.bot.ci.web;

import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.mock.web.DelegatingServletInputStream;

import com.daumcorp.commdev1.bot.ci.CIInterface;
import com.daumcorp.commdev1.bot.ci.CIListener;
import com.daumcorp.commdev1.bot.ci.dto.CIResult;
import com.daumcorp.commdev1.bot.ci.web.CIController;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class CIControllerTest {
	@Mock
	private HttpServletRequest req;
	@Mock
	private CIInterface ciInterface;
	@Spy
	@InjectMocks
	private CIController ciFailController = new CIController();

	@Test
	public void receiveBuild() throws Exception {
		when(req.getInputStream()).thenReturn(new DelegatingServletInputStream(getClass().getResourceAsStream("ci_noti.json")));

		ciFailController.receiveBuild(req);
		verify(ciFailController).notify("cafe-web", "http://ci.daumcorp.com/job/cafe-web/5");
	}

	@Test
	public void notifyFail() {
		CIListener listener = mock(CIListener.class);
		listeners().put(listener, Sets.newHashSet("cafe-web"));
		CIResult ciResult = new CIResult();
		ciResult.setSuccess(false);
		when(ciInterface.findBuild("http://ci.daumcorp.com/job/cafe-web/5")).thenReturn(ciResult);

		ciFailController.notify("cafe-web", "http://ci.daumcorp.com/job/cafe-web/5");

		verify(listener).onFail(ciResult);
	}

	@Test
	public void notifySuccess() {
		CIListener listener = mock(CIListener.class);
		listeners().put(listener, Sets.newHashSet("cafe-web"));
		CIResult ciResult = new CIResult();
		ciResult.setSuccess(true);
		when(ciInterface.findBuild("http://ci.daumcorp.com/job/cafe-web/5")).thenReturn(ciResult);

		ciFailController.notify("cafe-web", "http://ci.daumcorp.com/job/cafe-web/5");

		verify(listener).onSuccess(ciResult);
	}

	@Test
	public void notifyNothing() {
		CIListener listener = mock(CIListener.class);
		listeners().put(listener, Sets.newHashSet("cafe-web22"));
		CIResult ciResult = new CIResult();
		ciResult.setSuccess(true);
		when(ciInterface.findBuild("http://ci.daumcorp.com/job/cafe-web/5")).thenReturn(ciResult);

		ciFailController.notify("cafe-web", "http://ci.daumcorp.com/job/cafe-web/5");

		verify(listener, VerificationModeFactory.noMoreInteractions()).onSuccess(ciResult);
	}

	@SuppressWarnings("unchecked")
	private Map<CIListener, Set<String>> listeners() {
		return Whitebox.getInternalState(ciFailController, Map.class);
	}
}
