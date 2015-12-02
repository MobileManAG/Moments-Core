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
package com.mobileman.moments.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.mobileman.moments.core.domain.user.User;

@JsonInclude(value=Include.NON_NULL)
public class Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@NotBlank
	private String id;
	
	@Version
	private Long version;
	
	@CreatedBy
	@Field("created_by")
	private User createdBy;
	
	@LastModifiedBy
	@Field("modified_by")
	private User modifiedBy;
	
	@CreatedDate
	@JsonSerialize(using=DateSerializer.class)
	@Field("created_on")
	@NotNull
	private Date createdOn;
	
	@LastModifiedDate
	@JsonSerialize(using=DateSerializer.class)
	@Field("modified_on")
	private Date modifiedOn;
	
	public Entity(String _id) {
		super();
		this.id = _id;
	}
	
	public Entity() {
		super();
	}

	public String getId() {
		return id;
	}
	
	public void setId(String _id) {
		this.id = _id;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		Entity other = Entity.class.cast(obj);
		boolean equals = getId().equals(other.getId());
		return equals;
	}
	
	@Override
	public int hashCode() {
		int hashCode = this.getId().hashCode();
		return hashCode;
	}
	
}
