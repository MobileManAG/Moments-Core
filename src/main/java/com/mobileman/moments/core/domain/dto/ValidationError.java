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
package com.mobileman.moments.core.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ValidationError {

	private final List<FieldError> fieldErrors = new ArrayList<>();
	private final int status;
	private final String error;
	 
    public ValidationError(HttpStatus status) {
		this.status = status.value();
		this.error = "Validation error";
    }
 
    public void addFieldError(String path, String message) {
    	FieldError error = new FieldError(path, message);
        fieldErrors.add(error);
    }
    
    /**
	 * @return the fieldErrors
	 */
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getError() {
		return error;
	}

	public static class FieldError {
    	
    	private final String field;
    	 
        private final String message;
     
        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
        
    }
}
