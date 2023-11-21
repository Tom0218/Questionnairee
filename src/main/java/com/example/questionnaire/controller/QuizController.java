package com.example.questionnaire.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizSearchReq;

//RestController �غc��Restful API �n�[

@RestController
@CrossOrigin //��e��
public class QuizController {
	
	@Autowired
	private QuizService service;

	@PostMapping(value = "api/quiz/create")
	public QuizRes create(@RequestBody QuizReq req) {
		return service.create(req);
	}
	
	@GetMapping(value = "api/quiz/search")
	public QuizRes search( @RequestBody QuizSearchReq req) {
		return service.search(req.getTitle(),req.getStartDate(),req.getEndDate());
	}
	
	@PostMapping(value = "api/quiz/deleteQuestionnaire")
	public QuizRes deleteQuestionnaire(@RequestBody List<Integer> qnIdList) {
		return service.deleteQuestionnaire(qnIdList);
	}
	
	
}
