package com.example.questionnaire.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizDeleteQuesionnaireReq;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.UserReq;
import com.example.questionnaire.vo.UserRes;

//RestController 建構成Restful API 要加

@RestController
@CrossOrigin // 串前端
public class QuizController {

	@Autowired
	private QuizService service;

	@PostMapping(value = "api/quiz/update")
	public QuizRes update(@RequestBody QuizReq req) {
		return service.update(req);
	}

	@PostMapping(value = "api/quiz/create")
	public QuizRes create(@RequestBody QuizReq req) {
		return service.create(req);
	}

	@PostMapping(value = "api/quiz/deleteQuestionnaire")
	public QuizRes deleteQuestionnaire(@RequestBody QuizDeleteQuesionnaireReq req) {
		return service.deleteQuestionnaire(req.getQnIdList());
	}

	@PostMapping(value = "api/quiz/deleteQuestion")
	public QuizRes deleteQuestion(@RequestBody QuizDeleteQuesionnaireReq req) {
		return service.deleteQuestion(req.getQnId(), req.getQuIdList());
	}

	@GetMapping(value = "api/quiz/search")
	public QuizRes search(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		return service.search(title, startDate, endDate);

	}

	@GetMapping(value = "api/quiz/search1")
	public QuizRes search1(@RequestParam Map<String, Object> params) {
		Object paramTitle = params.get("title");
		Object paramStartDate = params.get("start_date");
		Object paramEndDate = params.get("end_date");
		String title = paramTitle != null ? (String) paramTitle : "";
		LocalDate startDate = paramStartDate != null ? LocalDate.parse((String) paramStartDate)
				: LocalDate.of(1971, 1, 1);
		LocalDate endDate = paramEndDate != null ? LocalDate.parse((String) paramStartDate)
				: LocalDate.of(2099, 12, 31);
		return service.search(title, startDate, endDate);
	}

	@GetMapping(value = "api/quiz/searchPublished")
	public QuestionnaireRes searchPublished(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		return service.searchQuestionnaireList(title, startDate, endDate, false);
	}
	
	@GetMapping(value = "api/quiz/searchQuestionnaire")
	public QuestionnaireRes searchQuestionnaire(@RequestParam int qnId) {
		return service.searchQuestionnaire(qnId);
	}

	@GetMapping(value = "api/quiz/searchQuestionList")
	public QuestionRes searchQuestionList(@RequestParam int qnId) {
		return service.searchQuestionList(qnId);
	}
	
	@PostMapping(value = "api/quiz/QuestionnaireSubmission")
	public UserRes QuestionnaireSubmission(@RequestBody UserReq req) {
		 return service.QuestionnaireSubmission(req);
	}
	
	@PostMapping(value = "api/quiz/Submission")
	public UserRes Submission(@RequestBody UserReq req) {
		 return service.Submission(req);
	}
	
	@GetMapping(value = "api/quiz/getSubmission")
	public UserRes getSubmission(@RequestParam int qnId) {
		return service.getSubmission(qnId);
		
	}
	
	
}
