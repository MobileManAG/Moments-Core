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
package com.mobileman.moments.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.mobileman.moments.core.services.notification.impl.parse.XParseAuthInterceptor;

@Configuration
public class ParseConfig {

	@Value("${parse.application_id}") 
	private String parseApplicationID;
	
	@Value("${parse.rest_api_key}") 
	private String parseRestApiKey;
	
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

	@Bean
    public RestTemplate parseRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new XParseAuthInterceptor(getParseApplicationID(), getParseRestApiKey())));
        return restTemplate;
    }
}
