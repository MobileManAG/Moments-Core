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
package com.mobileman.moments.core.services.report.impl;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.report.EntityType;
import com.mobileman.moments.core.domain.report.Report;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.utils.UserUtils;
import com.mobileman.moments.core.repositories.report.ReportRepository;
import com.mobileman.moments.core.services.impl.EntityServiceImpl;
import com.mobileman.moments.core.services.question.QuestionService;
import com.mobileman.moments.core.services.report.ReportService;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.util.view.ReportsCollectionFilter;
import com.mobileman.moments.rest.api.util.ApiUtils;

@Service
public class ReportServiceImpl extends EntityServiceImpl<Report> implements ReportService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;

	private ReportRepository reportRepository;
	
	@Autowired
	public void setQuestionRepository(
			ReportRepository reportRepository) {
		setEntityRespository(reportRepository);
		this.reportRepository = reportRepository;
	}
	
	private Slice<Report> findAllUnprocessedReports(Pageable pageable) {
		Query query = Query.query(Criteria
				.where("questionDeleted").is(Boolean.FALSE)
				.and("insightDeleted").is(Boolean.FALSE)
				.and("questionAuthorDeleted").is(Boolean.FALSE)
				.and("insightAuthorDeleted").is(Boolean.FALSE)
				.and("reportAuthorDeleted").is(Boolean.FALSE)
				).with(pageable.getSort());
		
		List<Report> allReports = this.mongoTemplate.find(query, Report.class);
		final Slice<Report> reports = new SliceImpl<Report>(allReports, pageable, false);
		return reports;
	}
	
	private Slice<Report> findAllReportsWithDeletedUser(Pageable pageable) {
		Query query = Query.query(new Criteria().orOperator(
				Criteria.where("questionDeleted").is(Boolean.TRUE),
				Criteria.where("insightDeleted").is(Boolean.TRUE),
				Criteria.where("questionAuthorDeleted").is(Boolean.TRUE),
				Criteria.where("insightAuthorDeleted").is(Boolean.TRUE),
				Criteria.where("reportAuthorDeleted").is(Boolean.TRUE))
				).with(pageable.getSort());
		List<Report> allReports = this.mongoTemplate.find(query, Report.class);
		final Slice<Report> reports = new SliceImpl<Report>(allReports, pageable, false);
		return reports;
	}
	
	@Override
	public Slice<Report> findAllReports(ReportsCollectionFilter filter) {
		Pageable pageableNew = new PageRequest(0, ApiUtils.MAX_PAGE_SIZE, Direction.DESC, "createdOn");

		final Slice<Report> reports;
		switch (filter) {
		case UNPROCESSED:
			reports = findAllUnprocessedReports(pageableNew);
			break;
		case INSIGHT_AUTHOR_DELETED:
			reports = this.reportRepository.findByInsightAuthorDeletedIsTrue(pageableNew);
			break;
		case INSIGHT_DELETED:
			reports = this.reportRepository.findByInsightDeletedIsTrue(pageableNew);
			break;
		case QUESTION_AUTHOR_DELETED:
			reports = this.reportRepository.findByQuestionAuthorDeletedIsTrue(pageableNew);
			break;
		case QUESTION_DELETED:
			reports = this.reportRepository.findByQuestionDeletedIsTrue(pageableNew);
			break;
		case REPORT_AUTHOR_DELETED:
			reports = this.reportRepository.findByReportAuthorDeletedIsTrue(pageableNew);
			break;
		case USER_DELETED:
			reports = findAllReportsWithDeletedUser(pageableNew);
			break;
		default:
			reports = findAllUnprocessedReports(pageableNew);
			break;
		}
		
		return reports;
	}

	@Override
	public Report deleteReportQuestion(Report report) {
		
		if (report.isQuestionDeleted()) {
			// already deleted return ok
			return report;
		}
		
		Question question = report.getQuestion();
		this.questionService.delete(question.getId());
		report.setQuestionDeleted(true);
		report.setInsightDeleted(true);
		save(report);
		
		return report;
	}
	
	@Override
	public Report deleteQuestionAuthor(Report report) {
		if (report.isQuestionAuthorDeleted()) {
			// already deleted return ok
			return report;
		}
		
		User questionAuthor = report.getQuestion().getCreatedBy();
		this.userService.delete(questionAuthor.getId());
		report.setQuestionAuthorDeleted(true);
		save(report);
		
		return report;
	}
	
	@Override
	public Report deleteReportAuthor(Report report) {
		if (report == null) {
			// already deleted return ok
			return report;
		}
		
		User reportAuthor = report.getQuestion().getCreatedBy();
		this.userService.delete(reportAuthor.getId());
		report.setReportAuthorDeleted(true);
		save(report);
		
		return report;
	}

	public Report reportQuestion(String questionId, User loggedUser) {
		if (loggedUser == null) {
			throw new ValidationException("Logged user does not exists");
		}
		
		Question question = this.questionService.findById(questionId);
		if (question == null) {
			throw new ValidationException("Question with id=" + questionId + " does not exists");
		}
		
		Report report = new Report();
		report.setCreatedBy(UserUtils.createBasicUser(loggedUser));
		report.setQuestion(question);
		report.setEntityType(EntityType.QUESTION);
		save(report);
		
		return report;
	}
	
	@Override
	public Report createReport(String id, EntityType type, User loggedUser) {
		if (loggedUser == null) {
			throw new ValidationException("Logged user does not exists");
		}
		
		final Report report;
		switch (type) {
		case QUESTION:
			report = reportQuestion(id, loggedUser);
			break;
		default:
			throw new ValidationException("Invalid report type = " + type + " to create");
		}
		
		return report;
	}

}
