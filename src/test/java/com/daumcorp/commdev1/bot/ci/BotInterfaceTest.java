package com.daumcorp.commdev1.bot.ci;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Ignore;
import org.junit.Test;

/**
 * OAuth를 통한 키 생성 과정을 테스트
 * 
 * @author jerry
 * 
 */
public class BotInterfaceTest {

	private static final String CONSUMER_KEY = "36f655ac-3bf6-46df-b087-281adacc66c3";
	private static final String CONSUMER_SECRET = "eFVVRNNV6j.i8zudg_Jc4GMRuuJELY7UpkupRdKuprc2yECsDJM2zA00";
	private static final String ACCESS_TOKEN = "a9212e4e-748d-4509-9d73-b2d1fd485ca0";
	private static final String ACCESS_TOKEN_SECRET = "FmE7fwfig7Pvri76M8_c7RCAduZcA68zfXyF7RfsH2MfUsgmpl6qHw00";
	private static final String TESTER_MP_ID = "PU_wST43zAR4TU0";
	private static final String TESTER_GROUP_ID = "GID18186517";
	private static final String TESTER_GROUP_ID2 = "GID18260972";

	@Test
	@Ignore
	public void regist() throws Exception {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

		// create an HTTP request to a protected resource
		HttpPost request = new HttpPost("https://apis.daum.net/mypeople/bot/register.json");

		BasicNameValuePair[] data = new BasicNameValuePair[] { new BasicNameValuePair("botName", "카페CI봇"),
				new BasicNameValuePair("receiveUrl", "http://cafe804.daum.net/bot/ci") };
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data), "UTF-8");
		request.setEntity(entity);

		// sign the request
		consumer.sign(request);

		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		is.close();
	}

	@Test
	@Ignore
	public void editProfile() throws Exception {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

		// create an HTTP request to a protected resource
		HttpPost request = new HttpPost("https://apis.daum.net/mypeople/profile/edit.json");

		BasicNameValuePair[] data = new BasicNameValuePair[] { new BasicNameValuePair("name", "CI봇"),
				new BasicNameValuePair("receiveUrl", "http://10.13.227.185/bot/ci") };
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data), "UTF-8");
		request.setEntity(entity);
		//		MultipartEntity multipartEntity = new MultipartEntity();
		//		multipartEntity.addPart("attach", new FileBody(new File("/Users/karian7/Downloads/CommunityDev1.png"), "image/png"));
		//		multipartEntity.addPart("name", new StringBody("CI봇", Charsets.UTF_8));
		//		request.setEntity(multipartEntity);
		// sign the request
		consumer.sign(request);

		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		is.close();
	}

	@Test
	@Ignore
	public void sendMessage() throws Exception {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

		// create an HTTP request to a protected resource
		HttpPost request = new HttpPost("https://apis.daum.net/mypeople/buddy/send.json");

		BasicNameValuePair[] data = new BasicNameValuePair[] { new BasicNameValuePair("buddyId", TESTER_MP_ID),
				new BasicNameValuePair("content", "hello~") };
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data), "UTF-8");
		request.setEntity(entity);

		// sign the request
		consumer.sign(request);

		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		is.close();
	}

	@Test
	@Ignore
	public void sendGroupMessage() throws Exception {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

		// create an HTTP request to a protected resource
		HttpPost request = new HttpPost("https://apis.daum.net/mypeople/group/send.json");

		BasicNameValuePair[] data = new BasicNameValuePair[] { new BasicNameValuePair("groupId", TESTER_GROUP_ID),
				new BasicNameValuePair("content", "hello~") };
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data), "UTF-8");
		request.setEntity(entity);

		// sign the request
		consumer.sign(request);

		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		is.close();
	}

	@Test
	@Ignore
	public void exitGroup() throws Exception {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

		// create an HTTP request to a protected resource
		HttpGet request = new HttpGet("https://apis.daum.net/mypeople/group/exit.json?groupId=" + TESTER_GROUP_ID2);

		// sign the request
		consumer.sign(request);

		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		is.close();
	}

}
