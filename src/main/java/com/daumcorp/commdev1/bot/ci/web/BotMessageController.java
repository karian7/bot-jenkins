package com.daumcorp.commdev1.bot.ci.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daumcorp.commdev1.bot.ci.BotCommand;
import com.daumcorp.commdev1.bot.ci.BotMessageReceiver;
import com.daumcorp.commdev1.bot.ci.dto.ReceiveMessage;
import com.daumcorp.commdev1.bot.ci.impl.Group;

@Controller
@RequestMapping("/ci")
public class BotMessageController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BotMessageReceiver botMessageReceiver;

	@RequestMapping
	@ResponseBody
	public void all(ReceiveMessage receiveMessage) {
		logger.error("UNDEFINED: " + ToStringBuilder.reflectionToString(receiveMessage, ToStringStyle.MULTI_LINE_STYLE));
	}

	@RequestMapping(params = "action=createGroup")
	@ResponseBody
	public void createGroup(ReceiveMessage receiveMessage) {
		logger.info(ToStringBuilder.reflectionToString(receiveMessage, ToStringStyle.MULTI_LINE_STYLE));
		botMessageReceiver.newGroup(receiveMessage.getGroupId());
	}

	@RequestMapping(params = {"action=sendFromGroup"})
	@ResponseBody
	public void sendFromGroup(ReceiveMessage receiveMessage) {
		logger.info(ToStringBuilder.reflectionToString(receiveMessage, ToStringStyle.MULTI_LINE_STYLE));
		switch (BotCommand.find(receiveMessage.getContent())) {
		case kick:
			botMessageReceiver.kick(receiveMessage.getGroupId());
			break;
		case addCI:
			botMessageReceiver.addCI(receiveMessage.getGroupId(), parseJobId(BotCommand.addCI, receiveMessage));
			break;
		case removeCI:
			botMessageReceiver.removeCI(receiveMessage.getGroupId(), parseJobId(BotCommand.removeCI, receiveMessage));
			break;
		case helpCI:
			Group group = new Group();
			group.setId(receiveMessage.getGroupId());
			group.welcome();
			break;
		default:
			break;
		}
	}

	@RequestMapping(params = "action=inviteToGroup")
	@ResponseBody
	public void welcome(ReceiveMessage receiveMessage) {
		logger.info(ToStringBuilder.reflectionToString(receiveMessage, ToStringStyle.MULTI_LINE_STYLE));
		if (receiveMessage.isCIBot()) {
			Group group = new Group();
			group.setId(receiveMessage.getGroupId());
			group.welcome();
		}
	}

	@RequestMapping(value = "/bot_test")
	public ModelAndView testView(HttpServletRequest req) throws IOException {
		new RuntimeException().printStackTrace();
		return new ModelAndView("bot_test");
	}

	private String parseJobId(BotCommand botCommand, ReceiveMessage receiveMessage) {
		String ciJobId = StringUtils.substringAfter(receiveMessage.getContent(), botCommand.message).trim();
		if (StringUtils.contains(ciJobId, " ")) {
			ciJobId = StringUtils.substringBefore(ciJobId, " ");
		}
		return ciJobId;
	}

}