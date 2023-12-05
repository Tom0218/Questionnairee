package com.example.questionnaire.controller;

import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpSession;

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

	@GetMapping(value = "api/quiz/searchQuestionList")
	public QuestionRes searchQuestionList(@RequestParam int qnId) {
		return service.searchQuestionList(qnId);
	}

	@PostMapping(value = "api/quiz/setAnswer")
	public QuizRes setAnswer(@RequestBody User user, HttpSession session) {
		QuizRes res = service.setInfoAndAnswer(user);
		if (res.getRtncode().getCode() == 200) {
			session.setAttribute("name", user.getName());
			session.setAttribute("age", user.getAge());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("phone", user.getPhoneNumber());
			session.setAttribute("quId", user.getqId());
			session.setAttribute("qnId", user.getQnId());
			session.setAttribute("answer", user.getAnswer());
			session.setAttribute("dateTime", user.getDateTime());
			return new QuizRes(res.getInfo(), RtnCode.SUCCESSFUL);
		}
		return new QuizRes(res.getInfo(), RtnCode.QUESTIONNAIRE_PARAM_ERROR);
	}

	@PostMapping(value = "api/quiz/dropAnswer")
	public QuizRes dropAnswer(@RequestBody User user, HttpSession session) {
		session.invalidate();
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@GetMapping(value = "api/quiz/getAnswer")
	public QuizRes getAnswer(@RequestBody User user, HttpSession session) {
	
		  	String name = (String) session.getAttribute("name");
		    String email = (String) session.getAttribute("email");
		    String phone = (String) session.getAttribute("phone");
		    Integer age = (Integer) session.getAttribute("age");
		    Integer quId = (Integer) session.getAttribute("quId");
		    Integer qnId = (Integer) session.getAttribute("qnId");
		    String answer = (String) session.getAttribute("answer");
//		    String dateTime = (String) session.getAttribute("dateTime");

		    System.out.println("Name: " + name);
		    System.out.println("Email: " + email);
		    System.out.println("Phone: " + phone);
		    System.out.println("Age: " + age);
		    System.out.println("QuId: " + quId);
		    System.out.println("QnId: " + qnId);
		    System.out.println("Answer: " + answer);
//		    System.out.println("DateTime: " + dateTime);
//		if (nameObj != null && emailObj != null && phoneObj != null && ageObj != null && quIdObj != null
//				&& qnIdObj != null && answerObj != null && dateTimeObj != null) {
//			String name = nameObj.toString();
//			String email = emailObj.toString();
//			String phone = phoneObj.toString();
//			int age = (Integer) ageObj;
//			int quId = (Integer) quIdObj;
//			int qnId = (Integer) qnIdObj;
//			String answer = answerObj.toString();
//			String dateString = dateTimeObj.toString();
//			LocalDate dateTime = LocalDate.parse(dateString);
//
//			return new QuizRes(null, RtnCode.SUCCESSFUL);
//		}
//		return new QuizRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

}
