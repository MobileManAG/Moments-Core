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
/**
 * MailConfig.java
 * 
 * Project: Moments
 * 
 * @author MobileMan GmbH
 * @date 20.3.2014
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.moments.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import com.mobileman.moments.core.services.mail.MailService;
import com.mobileman.moments.core.services.mail.impl.MailServiceImpl;


/**
 * @author MobileMan GmbH
 *
 */
@Configuration
public class MailConfig {
	
	@Value("${mail.host}") 
	private String mailHost;
	
	@Value("${mail.port}") 
	private int mailPort = 25;
	
	@Value("${mail.username}") 
	private String username;
	
	@Value("${mail.password}") 
	private String password;
	
	@Value("${mail.ssl.enable}") 
	private boolean enableSSL;
	
	/**
	 * @return velocityEngine
	 */
	@Bean
	public VelocityEngineFactoryBean velocityEngine() {
		VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
		bean.setResourceLoaderPath("classpath:/velocity");
		bean.setPreferFileSystemAccess(false);
		return bean;
	}
	
	/**
	 * @return mailSender
	 */
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl bean = new JavaMailSenderImpl();
		bean.setHost(mailHost);
		bean.setPort(mailPort);
		bean.setUsername(username);
		bean.setPassword(password);
		Properties javaMailProperties = new Properties();
		
		if (this.enableSSL) {
			javaMailProperties.setProperty("mail.smtp.auth", "true");
			javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
			javaMailProperties.put("mail.smtps.ssl.trust", "*");
		} else {
			javaMailProperties.setProperty("mail.smtp.auth", "false");
			javaMailProperties.setProperty("mail.smtp.starttls.enable", "false");
		}
		
		javaMailProperties.put("mail.debug", "true");
		javaMailProperties.setProperty("mail.mime.charset", "UTF8");
		bean.setJavaMailProperties(javaMailProperties);
		return bean;
	}
	
	/**
	 * @return mailService
	 */
	@Bean
	public MailService mailService() {
		MailServiceImpl bean = new MailServiceImpl();
		bean.setMailSender(mailSender());
		return bean;
	}
	
	/**
	 * @return messageSource
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		bean.setBasename("classpath:i18n/messages");
		return bean;
	}
}
