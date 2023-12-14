package com.example.questionnaire;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.UserReq;
import com.example.questionnaire.vo.UserRes;

@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService service;
	
	@Autowired
	private QuestionnaireDao qnDao;

	@Test
	public void creatTest() {
		Questionnaire questionnaire = new Questionnaire("trthrh", "testdescript", false, LocalDate.of(2023, 11, 26), LocalDate.of(2024, 12, 01));

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

//	@Test
//	public void updateTest() {
//		Questionnaire questionnaire = new Questionnaire(41, "test1Title", "testdescript", false,
//				LocalDate.of(2023, 11, 12), LocalDate.of(2023, 12, 22));
//		List<Question> questionList = new ArrayList<>();
//		Question q1 = new Question(3, 41, "test_question_1", "single", true, "我好;大家好");
//		questionList.addAll(Arrays.asList(q1));
//		QuizReq req = new QuizReq(questionnaire, questionList);
//		QuizRes res = service.update(req);
//		Assert.isTrue(res.getRtncode().getCode() == 200, "update error!");
//
//	}

	@Test
	public void deleteQuestionnaireTest() {
		QuizRes res = service.deleteQuestionnaire(Arrays.asList(22));
		Assert.isTrue(res.getRtncode().getCode() == 200, "delete error!");
	}

//	@Test
//	public void deleteQuestionTest() {
//		QuizRes res = service.deleteQuestion(41, Arrays.asList(1, 2));
//		Assert.isTrue(res.getRtncode().getCode() == 200, "delete question error!");
//
//	}

	@Test
	public void searchTest() {
		QuizRes res = service.search("D", LocalDate.of(2023, 12, 10), LocalDate.of(2023, 12, 29));
		Assert.isTrue(res.getRtncode().getCode() == 200, "upatet error!");
	}

//	@Test
//	public void searchQuestionnaireList() {
//		QuestionnaireRes res = service.searchQuestionnaireList(null, null, null, false);
//		System.out.println(res);
//	}
	
	@Test
	public void searchQnTest() {
		QuestionnaireRes res = service.searchQuestionnaire(93);
		System.out.println(res.getTitle());
	}
	
//	@Test
//	public void searchQuestionTest() {
//		QuestionRes res = service.searchQuestionList(86);
//		System.out.println(res.getQuestionList());
//		
//	}
	
	@Test
	public void insert() {
		int res = qnDao.insertData("qa_02", "qa_02 test", false, LocalDate.of(2023, 11, 26), LocalDate.of(2024, 12, 01));
		System.out.println(res);
		
		
	}

	@Test
	public void insertTest() {
		int res = qnDao.insertData("qa_02", "qa_02 test", false, LocalDate.of(2023, 11, 24), LocalDate.of(2024, 01, 24));
		System.out.println(res);
		
		
		
	}
	
//	@Test
//	public void upDateTest() {
//		int res = qnDao.updateData(52, "qn_007", "qn_007_test");
//		System.out.println(res);
//	}
//	@Test
//	public void sessionSetTest() {
//		
//		QuizRes res = service.setInfoAndAnswer(null ){
//			
//			System.out.println(res);
//		}
//	}
	
//	@Test
//	public void sessionGetTest() {
//		QuizRes res = service.
//	}
	
	@Test
	public void SubminTest() {
		List<User> submissionList = new ArrayList<>(); 
		User q1 = new User("tony","0963000700","sle@1289",22,13,2,"143;446",LocalDateTime.now());
		User q2 = new User("jenny","0963000010","qre@1235",22,13,2,"183;356",LocalDateTime.now());
		User q3 = new User("ben","0963000100","qwe@1236",22,13,2,"126;486",LocalDateTime.now());
		submissionList.addAll(Arrays.asList(q1,q2,q3));
		UserReq req = new UserReq(submissionList);
		UserRes res = service.Submission(req);
		System.out.println(res.getRtncode().getCode());
		Assert.isTrue(res.getRtncode().getCode() == 200, "create error!");
	}
	
	@Test
	public void findUserTest() {
		UserRes res = service.getSubmission(93);
		System.out.println(res.getRtncode().getCode());
	}
		
	
//	@Test
//	public void limitTest() {
//		 List<Questionnaire> res = qnDao.findWithLimitAndStartPosition(1, 3);
//		 res.forEach(item -> {
//			 System.out.println(item.getId());
//		 });
//	}

}
