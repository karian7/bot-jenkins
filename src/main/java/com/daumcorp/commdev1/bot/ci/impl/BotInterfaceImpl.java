package com.daumcorp.commdev1.bot.ci.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.daumcorp.commdev1.bot.ci.BotInterface;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

@Component
public class BotInterfaceImpl implements BotInterface {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String CONSUMER_KEY = "36f655ac-3bf6-46df-b087-281adacc66c3";
	private static final String CONSUMER_SECRET = "eFVVRNNV6j.i8zudg_Jc4GMRuuJELY7UpkupRdKuprc2yECsDJM2zA00";
	private static final String ACCESS_TOKEN = "a9212e4e-748d-4509-9d73-b2d1fd485ca0";
	private static final String ACCESS_TOKEN_SECRET = "FmE7fwfig7Pvri76M8_c7RCAduZcA68zfXyF7RfsH2MfUsgmpl6qHw00";

	@Override
	public void exitGroup(String groupId) {
		try {
			// create an HTTP request to a protected resource
			HttpGet request = new HttpGet("https://apis.daum.net/mypeople/group/exit.json?groupId=" + groupId);

			sign(request);

			// send the request
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			logAndClose(response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void sendGroupMessage(String groupId, String msg) {
		try {
			// create an HTTP request to a protected resource
			HttpPost request = new HttpPost("https://apis.daum.net/mypeople/group/send.json");

			request.setEntity(new UrlEncodedFormEntity(Lists.newArrayList(new BasicNameValuePair("groupId", groupId), new BasicNameValuePair("content", msg)), "UTF-8"));

			sign(request);

			// send the request
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			logAndClose(response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void logAndClose(HttpResponse response) throws IOException {
		logger.info("STATUS: {}", response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		logger.info(CharStreams.toString(new InputStreamReader(is)));
		Closeables.closeQuietly(is);
	}

	private void sign(HttpRequestBase request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
		consumer.sign(request);
	}

}
