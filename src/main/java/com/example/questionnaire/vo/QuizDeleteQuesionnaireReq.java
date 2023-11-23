package com.example.questionnaire.vo;

import java.util.List;

public class QuizDeleteQuesionnaireReq {
	
	private List<Integer> qnIdList;
	
	private List<Integer> quIdList;
	
	private int qnId;
	
	
	public int getQnId() {
		return qnId;
	}

	public void setQnId(int qnId) {
		this.qnId = qnId;
	}


	public List<Integer> getQnIdList() {
		return qnIdList;
	}

	public void setQnIdList(List<Integer> qnIdList) {
		this.qnIdList = qnIdList;
	}

	public List<Integer> getQuIdList() {
		return quIdList;
	}

	public void setQuIdList(List<Integer> quIdList) {
		this.quIdList = quIdList;
	}


	

	

}
