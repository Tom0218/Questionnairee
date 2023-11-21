package com.example.questionnaire;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService service;

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	// 完成
	@Test
	public void creatTest() {
		Questionnaire questionnaire = new Questionnaire("title2", "testdescript", false, LocalDate.of(2023, 12, 12),
				LocalDate.of(2023, 12, 22));

		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1, "test1_question_1", "single", false, "AAA;BBB;CCC");
		Question q2 = new Question(2, "test1_question_2", "single", false, "AAA;eee;CCC");
		Question q3 = new Question(3, "test1_question_3", "single", false, "AAA;fff;CCC");
		questionList.addAll(Arrays.asList(q1, q2, q3));

		QuizReq req = new QuizReq(questionnaire, questionList);
		QuizRes res = service.create(req);
		System.out.println(res.getRtncode().getCode());
		Assert.isTrue(res.getRtncode().getCode() == 200, "create error!");
	}

	// 完成
	@Test
	public void updateTest() {
		Questionnaire questionnaire = new Questionnaire(41, "test1Title", "testdescript", false,
				LocalDate.of(2023, 11, 12), LocalDate.of(2023, 12, 22));
		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(3, 41, "test_question_1", "single", true, "我好;大家好");
		questionList.addAll(Arrays.asList(q1));
		QuizReq req = new QuizReq(questionnaire, questionList);
		QuizRes res = service.update(req);
		Assert.isTrue(res.getRtncode().getCode() == 200, "update error!");

	}

	// 完成
	@Test
	public void deleteQuestionnaireTest() {
		QuizRes res = service.deleteQuestionnaire(Arrays.asList(22));
		Assert.isTrue(res.getRtncode().getCode() == 200, "delete error!");
	}

	// 完成
	@Test
	public void deleteQuestionTest() {
		QuizRes res = service.deleteQuestion(41, Arrays.asList(1, 2));
		Assert.isTrue(res.getRtncode().getCode() == 200, "delete question error!");

	}

	// 完成
	@Test
	public void searchTest() {
		QuizRes res = service.search(null, LocalDate.of(2023, 11, 24), LocalDate.of(2024, 1, 10));
		Assert.isTrue(res.getRtncode().getCode() == 200, "upatet error!");
	}

	@Test
	public void searchQuestionnaireList() {
		QuestionnaireRes res = service.searchQuestionnaireList(null, null, null, false);
	}

	@Test
	public void Test() {

	}

}
