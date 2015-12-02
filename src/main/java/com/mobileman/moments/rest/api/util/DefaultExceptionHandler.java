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
package com.mobileman.moments.rest.api.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mobileman.moments.core.domain.dto.ValidationError;
import com.mobileman.moments.rest.api.AbstractMomentsController;

@ControllerAdvice
public class DefaultExceptionHandler extends AbstractMomentsController {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler({MissingServletRequestParameterException.class,
            UnsatisfiedServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            ServletRequestBindingException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<String, Object> handleRequestException(Exception ex) {
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Request Error");
        map.put("cause", ex.getMessage());
        map.put("status", HttpStatus.BAD_REQUEST.value());
        return map;
    }
 
	@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public @ResponseBody Map<String, Object> handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex) throws IOException {
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Unsupported Media Type");
        map.put("cause", ex.getLocalizedMessage());
        map.put("supported", ex.getSupportedMediaTypes());
        map.put("status", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return map;
    }
	
	@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody Map<String, Object> handleValidationException(ValidationException ex) throws IOException {
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Validation exception");
        map.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }
	
	@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody ValidationError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) throws IOException {
		
		BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        
        ValidationError error = processFieldErrors(fieldErrors);
        
        return error;
    }
 
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Map<String, Object> handleUncaughtException(Exception ex) throws IOException {
    	log.error(ex.getMessage(), ex);
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Unknown Error");
        map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }
    
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody Map<String, Object> handleUncaughtException(AccessDeniedException ex) throws IOException {
    	log.error(ex.getMessage(), ex);
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Security exception");
        map.put("status", HttpStatus.FORBIDDEN.value());
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }
    
    
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody Map<String, Object> handleDuplicateKeyException(DuplicateKeyException ex) throws IOException {
    	log.error(ex.getMessage(), ex);
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Duplicate Key");
        map.put("status", HttpStatus.CONFLICT.value());
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }
    
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ExceptionHandler(DataRetrievalFailureException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody Map<String, Object> handleDataRetrievalFailureException(DataRetrievalFailureException ex) throws IOException {
    	log.error(ex.getMessage(), ex);
        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("error", "Data Retrieval Failure");
        map.put("status", HttpStatus.NOT_FOUND.value());
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }
}
