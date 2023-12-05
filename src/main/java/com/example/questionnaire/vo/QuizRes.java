package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.User;


public class QuizRes {
	
	private List<QuizVo> quizVoList;	//vo§¹¾ã°Ý¨÷
	
	private User info;
	
	

	private RtnCode rtncode;

	public QuizRes() {
		super();
	}

	public QuizRes(RtnCode rtncode) {
		super();
		this.rtncode = rtncode;
	}
	
	public QuizRes(List<QuizVo> quizVoList, RtnCode rtncode) {
		super();
		this.quizVoList = quizVoList;
		this.rtncode = rtncode;
	}
	
	

	public QuizRes(User info, RtnCode rtncode) {
		super();
		this.info = info;
		this.rtncode = rtncode;
	}

	public RtnCode getRtncode() {
		return rtncode;
	}

	public void setRtncode(RtnCode rtncode) {
		this.rtncode = rtncode;
	}

	public List<QuizVo> getQuizVoList() {
		return quizVoList;
	}

	public void setQuizVoList(List<QuizVo> quizVoList) {
		this.quizVoList = quizVoList;
	}

	public User getInfo() {
		return info;
	}

	public void setInfo(User info) {
		this.info = info;
	}

	
	
	
}
