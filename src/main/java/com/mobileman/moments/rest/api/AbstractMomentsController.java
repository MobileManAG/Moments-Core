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
package com.mobileman.moments.rest.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.mobileman.moments.core.domain.dto.ValidationError;

public abstract class AbstractMomentsController {

	protected Logger log = LoggerFactory.getLogger(getClass());
	
	public static final Locale LOCALE_DEFAULT = Locale.ENGLISH;
	
	
	@Autowired
	protected MessageSource messageSource;
	
	private List<String> getErrors(Errors errors) {
		List<String> messages = new ArrayList<>();
		for (ObjectError oe : errors.getAllErrors()) {
			messages.add(messageSource.getMessage(oe, LOCALE_DEFAULT));
		}
		return messages;
	}
	
	protected ResponseEntity<ModelMap> getErrorResponse(Errors errors) {
		return new ResponseEntity<ModelMap>(new ModelMap("errors", getErrors(errors)), HttpStatus.BAD_REQUEST);
	}
	
	protected ValidationError processFieldErrors(List<FieldError> fieldErrors) {
		ValidationError dto = new ValidationError(HttpStatus.UNPROCESSABLE_ENTITY);
 
        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveFieldErrorLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }
 
        return dto;
    }
	
	protected String resolveFieldErrorLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale =  LocaleContextHolder.getLocale();
        if (currentLocale == null) {
        	currentLocale = LOCALE_DEFAULT;
        }
        
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }
 
        return localizedErrorMessage;
    }
}
