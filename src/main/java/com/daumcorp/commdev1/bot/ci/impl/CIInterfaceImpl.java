package com.daumcorp.commdev1.bot.ci.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.daumcorp.commdev1.bot.ci.CIInterface;
import com.daumcorp.commdev1.bot.ci.dto.CIResult;
import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Component
public class CIInterfaceImpl implements CIInterface {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CIResult findBuild(String url) {
		return new RestTemplate().execute(url + "/api/json", HttpMethod.GET, null, new ResponseExtractor<CIResult>() {

			@Override
			public CIResult extractData(ClientHttpResponse response) throws IOException {
				return parseBuild(response.getBody());
			}

		}, new Object[] {});
	}

	@Override
	public boolean existsCI(String ciJobId) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			protected boolean hasError(HttpStatus statusCode) {
				return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
			}
		});

		return restTemplate.execute("http://ci.daumcorp.com/job/{ciJobId}/api/json", HttpMethod.GET, null, new ResponseExtractor<Boolean>() {

			@Override
			public Boolean extractData(ClientHttpResponse response) throws IOException {
				return response.getRawStatusCode() == 200;
			}

		}, new Object[] { ciJobId });
	}

	CIResult parseBuild(InputStream is) throws IOException {
		String json = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8)).trim();
		JsonElement jsonElement;
		try {
			jsonElement = new JsonParser().parse(json);
		} catch (JsonSyntaxException e) {
			logger.error("JSON: {}", json);
			throw e;
		}
		CIResult result = new CIResult();
		result.setUrl(jsonElement.getAsJsonObject().get("url").getAsString());
		result.setSuccess(StringUtils.equals(jsonElement.getAsJsonObject().get("result").getAsString(), "SUCCESS"));
		result.setJobId(StringUtils.substringBefore(jsonElement.getAsJsonObject().get("fullDisplayName").getAsString(), " "));
		for (JsonElement participant : Iterables.getLast(jsonElement.getAsJsonObject().get("actions").getAsJsonArray()).getAsJsonObject().get("participants").getAsJsonArray()) {
			result.getParticipants().add(participant.getAsJsonObject().get("fullName").getAsString());
		}
		return result;
	}
}
