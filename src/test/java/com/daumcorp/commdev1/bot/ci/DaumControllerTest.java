package com.daumcorp.commdev1.bot.ci;

import static org.springframework.test.web.ModelAndViewAssert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.daumcorp.commdev1.bot.ci.web.BotMessageController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/daum-servlet-context.xml")
public class DaumControllerTest {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HandlerAdapter handlerAdapter;

	@Autowired
	private BotMessageController daumController;

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		handlerAdapter = new AnnotationMethodHandlerAdapter();
	}

	@Test
	@Ignore
	public void startTest() throws Exception {

		request.setRequestURI("/");
		request.setMethod("GET");

		ModelAndView daumModelAndView = handlerAdapter.handle(request, response, daumController);

		assertViewName(daumModelAndView, "index");
	}
}
