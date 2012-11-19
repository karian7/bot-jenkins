package com.daumcorp.commdev1.bot.ci;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daumcorp.commdev1.bot.ci.dto.ReceiveMessage;
import com.daumcorp.commdev1.bot.ci.web.BotMessageController;

@RunWith(MockitoJUnitRunner.class)
public class BotMessageControllerSendFromGroupTest {
	@Mock
	private BotMessageReceiver botMessageReceiver;

	@InjectMocks
	private final BotMessageController botMessageController = new BotMessageController();

	@Test
	public void sendFromGroupForKick() {
		ReceiveMessage receiveMessage = new ReceiveMessage();
		receiveMessage.setAction("sendFromGroup");
		receiveMessage.setGroupId("GRPID");
		receiveMessage.setContent(BotCommand.kick.message);
		botMessageController.sendFromGroup(receiveMessage);

		verify(botMessageReceiver).kick("GRPID");
	}

	@Test
	public void sendFromGroupForAddCI() {
		ReceiveMessage receiveMessage = new ReceiveMessage();
		receiveMessage.setAction("sendFromGroup");
		receiveMessage.setGroupId("GRPID");
		receiveMessage.setContent(BotCommand.addCI.message + " cafe-web");
		botMessageController.sendFromGroup(receiveMessage);

		verify(botMessageReceiver).addCI("GRPID", "cafe-web");

		receiveMessage.setContent(BotCommand.addCI.message + " cafe-web 블라블라 ");
		botMessageController.sendFromGroup(receiveMessage);
		verify(botMessageReceiver, times(2)).addCI("GRPID", "cafe-web");

		receiveMessage.setContent("이러쿵저러쿵 " + BotCommand.addCI.message + " cafe-web 블라블라");
		botMessageController.sendFromGroup(receiveMessage);
		verify(botMessageReceiver, times(3)).addCI("GRPID", "cafe-web");
	}

	@Test
	public void sendFromGroupForRemoveCI() {
		ReceiveMessage receiveMessage = new ReceiveMessage();
		receiveMessage.setAction("sendFromGroup");
		receiveMessage.setGroupId("GRPID");
		receiveMessage.setContent(BotCommand.removeCI.message + " cafe-web");
		botMessageController.sendFromGroup(receiveMessage);

		verify(botMessageReceiver).removeCI("GRPID", "cafe-web");
	}
}