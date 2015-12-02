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
package com.mobileman.moments.rest.api.auth.v1.report;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobileman.moments.core.domain.dto.report.CreateReportDto;
import com.mobileman.moments.core.domain.report.Report;
import com.mobileman.moments.core.services.report.ReportService;
import com.mobileman.moments.core.util.security.SecurityUtils;
import com.mobileman.moments.core.util.view.ReportsCollectionFilter;
import com.mobileman.moments.rest.api.AbstractMomentsController;
import com.mobileman.moments.rest.api.MomentsRestURIConstants;

@RestController
@RequestMapping(
	value = MomentsRestURIConstants.API_AUTH_V1 + "/reports", 
	consumes = MediaType.APPLICATION_JSON_VALUE, 
	produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController extends AbstractMomentsController {

	@Autowired
	private ReportService reportService;
	
	@RequestMapping(value="", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE}, method = RequestMethod.GET)
	@RolesAllowed({"ROLE_ADMIN"})
	public Slice<Report> findAllReports(@RequestParam(required=false, defaultValue="0") Integer filter) {
		Slice<Report> crowd = this.reportService.findAllReports(filter == null ? null : ReportsCollectionFilter.fromJson(filter.intValue()));
		return crowd;
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<Report> deleteReport(@PathVariable("id") Report report) {
		if (report == null) {
			throw new ValidationException("Report does not exists");
		}
		
		this.reportService.delete(report);
		return new ResponseEntity<Report>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/{id}/question", method = RequestMethod.DELETE)
	@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<Report> deleteReportQuestion(@PathVariable("id") Report report) {
		if (report == null) {
			throw new ValidationException("Report does not exists");
		}
		
		Report reportUpdated = this.reportService.deleteReportQuestion(report);
		return new ResponseEntity<Report>(reportUpdated, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/question_author", method = RequestMethod.DELETE)
	@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<Report> deleteQuestionAuthor(@PathVariable("id") Report report) {
		if (report == null) {
			throw new ValidationException("Report does not exists");
		}
		
		Report reportUpdated = this.reportService.deleteQuestionAuthor(report);
		return new ResponseEntity<Report>(reportUpdated, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/report_author", method = RequestMethod.DELETE)
	@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<Report> deleteReportAuthor(@PathVariable("id") Report report) {
		if (report == null) {
			throw new ValidationException("Report does not exists");
		}
		
		Report reportUpdated = this.reportService.deleteReportAuthor(report);
		return new ResponseEntity<Report>(reportUpdated, HttpStatus.OK);
	}
	
	@RequestMapping(value="", method = RequestMethod.POST)
	public ResponseEntity<Report> createReport(@Valid @RequestBody CreateReportDto data) {
		Report report = this.reportService.createReport(data.getId(), data.getType(), SecurityUtils.getLoggedUser());
		return new ResponseEntity<Report>(report, HttpStatus.CREATED);
	}
}
