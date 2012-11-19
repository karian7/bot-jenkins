package com.daumcorp.commdev1.bot.ci;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.context.ApplicationContext;

import com.daumcorp.commdev1.bot.ci.impl.Group;
import com.daumcorp.commdev1.bot.ci.spring.ApplicationContextProvider;

@RunWith(MockitoJUnitRunner.class)
public class GroupEventListenerTest {
	@Mock
	private BotInterface botInterface;

	private GroupEventListener groupEventListener = new Group();

	@Test
	public void kick() {

		ApplicationContext mock = mock(ApplicationContext.class);
		when(mock.getBean(BotInterface.class)).thenReturn(botInterface);
		new ApplicationContextProvider().setApplicationContext(mock);

		Whitebox.setInternalState(groupEventListener, "id", "GRPID");
		groupEventListener.kick();

		verify(botInterface).exitGroup("GRPID");
	}
}
