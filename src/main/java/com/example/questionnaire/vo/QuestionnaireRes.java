package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Questionnaire;

public class QuestionnaireRes extends Questionnaire {

	private Questionnaire questionnaire;

	private List<Questionnaire> questionnaireList;

	private RtnCode rtncode;

	public QuestionnaireRes() {
		super();
	}

	public QuestionnaireRes(RtnCode rtncode) {
		super();
		this.rtncode = rtncode;
	}

	public QuestionnaireRes(Questionnaire questionnaire, RtnCode rtncode) {
		super();
		this.questionnaire = questionnaire;
		this.rtncode = rtncode;
	}

	public QuestionnaireRes(List<Questionnaire> questionnaireList, RtnCode rtncode) {
		super();
		this.questionnaireList = questionnaireList;
		this.rtncode = rtncode;
	}

	public List<Questionnaire> getQuestionnaireList() {
		return questionnaireList;
	}

	public void setQuestionnaireList(List<Questionnaire> questionnaireList) {
		this.questionnaireList = questionnaireList;
	}

	public RtnCode getRtncode() {
		return rtncode;
	}

	public void setRtncode(RtnCode rtncode) {
		this.rtncode = rtncode;
	}

}
