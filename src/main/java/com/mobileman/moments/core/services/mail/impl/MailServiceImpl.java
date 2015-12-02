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
package com.mobileman.moments.core.services.mail.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.mail.MailService;
import com.mobileman.moments.core.util.locale.LocalizationUtils;

/**
 * @author MobileMan GmbH
 *
 */
@Service()
public class MailServiceImpl implements MailService {
	
	private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	
	private static final String EMAIL_ENCODING = "UTF-8";
	
	private JavaMailSender mailSender;
	
	@Value("${mail.systemAdminEmail}") 
	private String systemAdminEmail;
	
	@Value("${mail.supportEmail}") 
	private String supportEmail;
	
	@Value("${mail.spamEmail}") 
	private String spamEmail;
	
	@Value("${mail.contactEmail}") 
	private String contactEmail;
	
	@Value("${mail.noreplyEmail}") 
	private String noreplyEmail;

	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	/**
	 *
	 * @return velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return this.velocityEngine;
	}

	/**
	 *
	 * @param mailSender mailSender
	 */
	@Autowired
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	/**
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/**
	 * @return the systemAdminEmail
	 */
	public String getSystemAdminEmail() {
		return systemAdminEmail;
	}

	/**
	 * @return the supportEmail
	 */
	public String getSupportEmail() {
		return supportEmail;
	}

	/**
	 * @return the spamEmail
	 */
	public String getSpamEmail() {
		return spamEmail;
	}

	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * @return the noreplyEmail
	 */
	public String getNoreplyEmail() {
		return noreplyEmail;
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Async
	public void sendResetCredientialsEmail(final User user, final String password) {
		if (log.isDebugEnabled()) {
			log.debug("sendResetCredientialsEmail(" + user + ") - start");
		}
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			
			@Override
            public void prepare(javax.mail.internet.MimeMessage mimeMessage) throws Exception {
				if (log.isDebugEnabled()) {
					log.debug("sendResetCredientialsEmail$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());
				messageHelper.setTo(user.getAccount().getEmail());
				messageHelper.setFrom(getNoreplyEmail());
				
				String subject = messageSource.getMessage("email.forgot_password.subject", null, LocalizationUtils.DEFAULT_LOCALE);
				messageHelper.setSubject(subject);
				
				Map<String, Object> model = new HashMap<String, Object>();
                model.put("user", user);
                model.put("password", password);
                
                String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
                		getVelocityEngine(), "reset-credentials-email-body.vm", EMAIL_ENCODING, model);
                String textMessage = VelocityEngineUtils.mergeTemplateIntoString(
                		getVelocityEngine(), "reset-credentials-email-body-text.vm", EMAIL_ENCODING, model);
                
                messageHelper.setText(textMessage, htmlMessage);

				if (log.isDebugEnabled()) {
					log.debug("sendResetCredientialsEmail$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
				}
            }
        };
        
        this.mailSender.send(preparator);
        
        if (log.isDebugEnabled()) {
			log.debug("sendResetCredientialsEmail(...) - end");
		}
	}

}
