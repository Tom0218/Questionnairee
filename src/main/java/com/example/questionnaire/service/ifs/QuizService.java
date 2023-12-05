package com.example.questionnaire.service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.questionnaire.entity.User;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;

public interface QuizService {

	public QuizRes create(QuizReq req);

	public QuizRes update(QuizReq req);

	public QuizRes deleteQuestionnaire(List<Integer> qnIdList);

	public QuizRes deleteQuestion(int qnId, List<Integer> quIdList);

	public QuizRes search(String title, LocalDate startDate, LocalDate endDate);

	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,
			boolean isPublished);

	public QuestionRes searchQuestionList(int qnId);
	
	public QuizRes  setInfoAndAnswer(User user);


}
