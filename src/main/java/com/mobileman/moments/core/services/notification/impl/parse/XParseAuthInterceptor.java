/*******************************************************************************
 * Copyright 2015 MobileMan GmbH
 * www.mobileman.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mobileman.moments.core.services.notification.impl.parse;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class XParseAuthInterceptor implements ClientHttpRequestInterceptor {

	private String parseApplicationID;
	private String parseRestApiKey;

	public XParseAuthInterceptor(String parseApplicationID,
			String parseRestApiKey) {
		this.parseApplicationID = parseApplicationID;
		this.parseRestApiKey = parseRestApiKey;

	}

	
	/**
	 * @return the parseApplicationID
	 */
	public String getParseApplicationID() {
		return parseApplicationID;
	}


	/**
	 * @return the parseRestApiKey
	 */
	public String getParseRestApiKey() {
		return parseRestApiKey;
	}


	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.add("X-Parse-Application-Id", getParseApplicationID());
		headers.add("X-Parse-REST-API-Key", getParseRestApiKey());
		return execution.execute(request, body);
	}

}
