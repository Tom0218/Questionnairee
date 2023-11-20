package com.example.questionnaire.entity;

import java.io.Serializable;

public class QuestionId implements Serializable{
	
	private int quId;
	
	private int qnId;

	public QuestionId() {
		super();
	}


	public QuestionId(int quId, int qnId) {
		super();
		this.quId = quId;
		this.qnId = qnId;
	}


	public int getQuid() {
		return quId;
	}

	public void setQuid(int quId) {
		this.quId = quId;
	}



	public int getQnId() {
		return qnId;
	}



	public void setQnId(int qnId) {
		this.qnId = qnId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

	
}
