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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan(basePackages = {"com.mobileman.moments.core"})
public class IntegrationConfig {

	@Bean
	public MessageChannel entityCreatedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel entityDeletedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel questionCreatedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel questionDeletedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel gcmChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel insightCreatedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel insightDeletedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel insightLikeCreatedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
	@Bean
	public MessageChannel insightLikeDeletedChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		channel.setIgnoreFailures(true);
		return channel;
	}
	
}
