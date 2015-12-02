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
package com.mobileman.moments.core.domain.report;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mobileman.moments.core.domain.Entity;
import com.mobileman.moments.core.domain.question.Question;


@Document
public class Report extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Field("entity_type")
	private EntityType entityType;

	private Question question;
	
	@Field("question_deleted")
	private boolean questionDeleted;
	
	@Field("insight_deleted")
	private boolean insightDeleted;
	
	@Field("question_author_deleted")
	private boolean questionAuthorDeleted;
	
	@Field("insight_author_deleted")
	private boolean insightAuthorDeleted;
	
	@Field("report_author_deleted")
	private boolean reportAuthorDeleted;

	/**
	 * @return the type
	 */
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * @param type the type to set
	 */
	public void setEntityType(EntityType type) {
		this.entityType = type;
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the questionDeleted
	 */
	public boolean isQuestionDeleted() {
		return questionDeleted;
	}

	/**
	 * @param questionDeleted the questionDeleted to set
	 */
	public void setQuestionDeleted(boolean questionDeleted) {
		this.questionDeleted = questionDeleted;
	}

	/**
	 * @return the insightDeleted
	 */
	public boolean isInsightDeleted() {
		return insightDeleted;
	}

	/**
	 * @param insightDeleted the insightDeleted to set
	 */
	public void setInsightDeleted(boolean insightDeleted) {
		this.insightDeleted = insightDeleted;
	}

	/**
	 * @return the questionAuthorDeleted
	 */
	public boolean isQuestionAuthorDeleted() {
		return questionAuthorDeleted;
	}

	/**
	 * @param questionAuthorDeleted the questionAuthorDeleted to set
	 */
	public void setQuestionAuthorDeleted(boolean questionAuthorDeleted) {
		this.questionAuthorDeleted = questionAuthorDeleted;
	}

	/**
	 * @return the insightAuthorDeleted
	 */
	public boolean isInsightAuthorDeleted() {
		return insightAuthorDeleted;
	}

	/**
	 * @param insightAuthorDeleted the insightAuthorDeleted to set
	 */
	public void setInsightAuthorDeleted(boolean insightAuthorDeleted) {
		this.insightAuthorDeleted = insightAuthorDeleted;
	}

	/**
	 * @return the reportAuthorDeleted
	 */
	public boolean isReportAuthorDeleted() {
		return reportAuthorDeleted;
	}

	/**
	 * @param reportAuthorDeleted the reportAuthorDeleted to set
	 */
	public void setReportAuthorDeleted(boolean reportAuthorDeleted) {
		this.reportAuthorDeleted = reportAuthorDeleted;
	}
	
	
}
