package com.example.questionnaire.controller;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuizDeleteQuesionnaireReq;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizSearchReq;


//RestController 建構成Restful API 要加

@RestController
@CrossOrigin // 串前端
public class QuizController {

	@Autowired
	private QuizService service;

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
		return service.deleteQuestion(req.getQnId(),req.getQuIdList());
	}

	@GetMapping(value = "api/quiz/search")
	public QuizRes search(@RequestBody QuizSearchReq req) {
		String title = StringUtils.hasText(req.getTitle()) ? req.getTitle():"";
		LocalDate startDate = req.getStartDate() != null ? req.getStartDate() : LocalDate.of(1971, 1, 1);
		LocalDate endDate = req.getEndDate() != null ? req.getEndDate() : LocalDate.of(2099, 12, 31);
		return service.search(title, startDate, endDate);
		//return service.search(req.getTitle(), req.getStartDate(), req.getEndDate());
	}

}
