package com.example.questionnaire;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;

@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService service;
	

	
	@Test
	public void creatTest() {
		Questionnaire questionnaire = new Questionnaire("title1","testdescript", false, LocalDate.of(2023, 11, 17),
				LocalDate.of(2023, 11, 30));
		
		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1,"test_question_1","single",false,"AAA;BBB;CCC");
		Question q2 = new Question(2,"test_question_2","single",false,"AAA;eee;CCC");
		Question q3 = new Question(3,"test_question_3","single",false,"AAA;fff;CCC");
		questionList.addAll(Arrays.asList(q1,q2,q3));
		
		QuizReq req = new QuizReq(questionnaire,questionList);
		QuizRes res = service.create(req);
		System.out.println(res.getRtncode().getCode());
		Assert.isTrue(res.getRtncode().getCode()==200,"create error!");
	}

	@Test
	public void updateTest() {
		Questionnaire questionnaire = new Questionnaire(7,"test1Title", "testdescript", false, LocalDate.of(2023, 11, 22),
				LocalDate.of(2023, 12, 15));
		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1,7,"test_question_1","single",true,"你好;我好;大家好");
		questionList.addAll(Arrays.asList(q1));
		QuizReq req = new QuizReq(questionnaire,questionList);
		QuizRes res = service.create(req);	
		Assert.isTrue(res.getRtncode().getCode()==200,"upatet error!");

	}
	

	@Test
	public void deleteQuestionnaireTest() {
		List<Integer> qnIdList = Arrays.asList(7,8);
		QuizRes res = service.deleteQuestionnaire(qnIdList);
		Assert.isTrue(res.getRtncode().getCode() == 200, "Delete Questionnaire failed!");
	}
		
}
