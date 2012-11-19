/**
 * 
 */
package com.daumcorp.commdev1.bot.ci;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author karian7
 * 
 */
public class CIRemoteAPI {
	@Test
	@Ignore
	public void test() throws Exception {
		// http://ci.daumcorp.com/job/cafe-web/api/json?pretty=true
		JsonElement element = new RestTemplate().execute("http://ci.daumcorp.com/job/cafe-web/api/json", HttpMethod.GET, null, new ResponseExtractor<JsonElement>() {

			@Override
			public JsonElement extractData(ClientHttpResponse response) throws IOException {
				return new JsonParser().parse(new InputStreamReader(response.getBody()));
			}
		}, new Object[] {});
		System.out.println(element.getAsJsonObject().getAsJsonObject("lastBuild").get("url"));
	}
}
