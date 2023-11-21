package com.example.questionnaire.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizVo {
	///�@�i�ݨ��h�ӵ��סA�@��QuizVo == �@�Ӱݨ�

	
	private Questionnaire questionnaire = new Questionnaire();//�@�i�ݨ�

	@JsonProperty("question_List")
	private List<Question> questionList= new ArrayList<>();//�D��

	public QuizVo() {
		super();
		
	}

	public QuizVo(Questionnaire questionnaire, List<Question> questionList) {
		super();
		this.questionnaire = questionnaire;
		this.questionList = questionList;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

	

}
