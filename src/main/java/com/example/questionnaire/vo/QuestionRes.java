package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;

public class QuestionRes {
	
	private RtnCode rtncode;
	
	private List<Question> questionList;
	

	public QuestionRes() {
		super();
	}

	public QuestionRes(List<Question> questionList, RtnCode rtncode) {
		super();
		this.rtncode = rtncode;
		this.questionList = questionList;
	}

	public RtnCode getRtncode() {
		return rtncode;
	}

	public void setRtncode(RtnCode rtncode) {
		this.rtncode = rtncode;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}
	
	

}
