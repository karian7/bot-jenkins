/**
 * 
 */
package com.daumcorp.commdev1.bot.ci.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.context.ApplicationContext;

import com.daumcorp.commdev1.bot.ci.BotInterface;
import com.daumcorp.commdev1.bot.ci.BotMessageReceiver;
import com.daumcorp.commdev1.bot.ci.CIInterface;
import com.daumcorp.commdev1.bot.ci.CIListener;
import com.daumcorp.commdev1.bot.ci.CIMessageReceiver;
import com.daumcorp.commdev1.bot.ci.GroupEventListener;
import com.daumcorp.commdev1.bot.ci.spring.ApplicationContextProvider;

/**
 * @author karian7
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class BotMessageReceiverImplTest {
	@Mock
	private CIMessageReceiver ciMessageReceiver;
	@Mock
	private CIInterface ciInterface;

	@InjectMocks
	private final BotMessageReceiver botMessageReceiver = new BotMessageReceiverImpl();

	@Before
	public void setUp() {
		when(ciInterface.existsCI("cafe-web")).thenReturn(true);
		ApplicationContext mock = mock(ApplicationContext.class);
		when(mock.getBean(BotInterface.class)).thenReturn(mock(BotInterface.class));
		new ApplicationContextProvider().setApplicationContext(mock);
	}

	@Test
	public void kick() {
		listeners().put("GRPID", mock(GroupEventListener.class, withSettings().extraInterfaces(CIListener.class)));
		GroupEventListener listener = botMessageReceiver.kick("GRPID");

		verify(listener).kick();
		verify(ciMessageReceiver).removeListener(listener, null);
		assertThat(listeners().isEmpty(), is(true));
	}

	@Test
	public void newGroup() {
		botMessageReceiver.newGroup("GRPID");
		assertThat(listeners(), notNullValue(Map.class));
		assertThat(listeners().size(), is(1));

		verify(ciMessageReceiver).addListener(listeners().get("GRPID"), null);
	}

	@Test
	public void addCI() {
		listeners().put("GRPID", mock(GroupEventListener.class, withSettings().extraInterfaces(CIListener.class)));
		botMessageReceiver.addCI("GRPID", "cafe-web");

		verify(ciMessageReceiver).addListener(listeners().get("GRPID"), "cafe-web");
	}
	
	@Test
	public void addCIWhenNotExists() {
		listeners().put("GRPID", mock(GroupEventListener.class, withSettings().extraInterfaces(CIListener.class)));
		botMessageReceiver.addCI("GRPID", "cafe-web111");
		
		verify(ciMessageReceiver, VerificationModeFactory.noMoreInteractions()).addListener(listeners().get("GRPID"), "cafe-web");
	}

	@Test
	public void removeCI() {
		listeners().put("GRPID", mock(GroupEventListener.class, withSettings().extraInterfaces(CIListener.class)));
		botMessageReceiver.removeCI("GRPID", "cafe-web");

		verify(ciMessageReceiver).removeListener(listeners().get("GRPID"), "cafe-web");
	}

	@SuppressWarnings("unchecked")
	private Map<String, GroupEventListener> listeners() {
		return Whitebox.getInternalState(botMessageReceiver, Map.class);
	}
}
