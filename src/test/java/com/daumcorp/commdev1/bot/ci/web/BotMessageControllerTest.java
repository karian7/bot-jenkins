package com.daumcorp.commdev1.bot.ci.web;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daumcorp.commdev1.bot.ci.BotMessageReceiver;

import com.daumcorp.commdev1.bot.ci.dto.ReceiveMessage;
import com.daumcorp.commdev1.bot.ci.web.BotMessageController;

@RunWith(MockitoJUnitRunner.class)
public class BotMessageControllerTest {
	@Mock
	private BotMessageReceiver botMessageReceiver;

	@InjectMocks
	private final BotMessageController botMessageController = new BotMessageController();

	@Test
	public void createGroup() {
		ReceiveMessage receiveMessage = new ReceiveMessage();
		receiveMessage.setAction("createGroup");
		receiveMessage.setGroupId("GRPID");
		receiveMessage.setContent("[{\"buddyId\": \"BUDID\", \"name\": \"광수\"}]");
		botMessageController.createGroup(receiveMessage);

		verify(botMessageReceiver).newGroup(eq("GRPID"));
	}
}