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
package com.mobileman.moments.core.services.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mobileman.moments.core.domain.Entity;
import com.mobileman.moments.core.repositories.EntityRespository;
import com.mobileman.moments.core.services.EntityService;
import com.mongodb.WriteResult;

public abstract class EntityServiceImpl<T extends Entity> implements EntityService<T> {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private EntityRespository<T> entityRespository;
	private Class<T> entityType;

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityServiceImpl() {
		super();
		if (ParameterizedType.class.isInstance(getClass().getGenericSuperclass())) {
			java.lang.reflect.Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			if (java.lang.reflect.TypeVariable.class.isInstance(type)) {
				TypeVariable typeVariable = TypeVariable.class.cast(type);
				this.entityType = (Class<T>)typeVariable.getBounds()[0];
			} else {
				this.entityType = (Class<T>)type;
			}
		} else {
			throw new IllegalArgumentException("Type does not extends moments Entity");
		}
	}
	
	public Logger getLog() {
		return log;
	}
		
	public Class<T> getEntityType() {
		return entityType;
	}
	
	public void setEntityRespository(EntityRespository<T> entityRespository) {
		this.entityRespository = entityRespository;
	}
	
	public EntityRespository<T> getEntityRespository() {
		return entityRespository;
	}
	
	@Override
	public T findById(String id) {
		return entityRespository.findOne(id);
	}
	
	@Override
	public boolean delete(String id) {
		return delete(findById(id));
	}
	
	@Override
	public boolean delete(T entity) {
		if (entity == null) {
			return false;
		}
		
		WriteResult result = this.mongoTemplate.remove(Query.query(Criteria.where("_id").is(entity.getId())), getEntityType());
		if (result.getN() == 0) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public T save(T entity) {
		this.entityRespository.save(entity);
		return entity;
	}
}
