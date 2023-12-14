package com.example.questionnaire.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.questionnaire.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserReq extends User {

	@JsonProperty("Submission_List")
	private List<User> UserSubmissionList = new ArrayList<>();

	@JsonProperty("quId_list")
	private List<Integer> quIdList = new ArrayList<>();

	private String name;

	private String phoneNumber;

	private String email;

	private int age;

	private int qnId;

	private int quId;

	private String answer;

	private LocalDateTime dateTime;

	public UserReq() {
		super();
	}

	public UserReq(int qnId, List<Integer> quIdList) {
		super();
		this.quIdList = quIdList;
		this.qnId = qnId;
	}

	public UserReq(List<User> userSubmissionList) {
		super();
		UserSubmissionList = userSubmissionList;
	}

	public UserReq(String name, String phoneNumber, String email, int age, int qnId, int quId, String answer,
			LocalDateTime dateTime) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.age = age;
		this.qnId = qnId;
		this.quId = quId;
		this.answer = answer;
		this.dateTime = dateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getQnId() {
		return qnId;
	}

	public void setQnId(int qnId) {
		this.qnId = qnId;
	}

	

	public int getQuId() {
		return quId;
	}

	public void setQuId(int quId) {
		this.quId = quId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public List<User> getUserSubmissionList() {
		return UserSubmissionList;
	}

	public void setUserSubmissionList(List<User> userSubmissionList) {
		UserSubmissionList = userSubmissionList;
	}

	public List<Integer> getQuIdList() {
		return quIdList;
	}

	public void setQuIdList(List<Integer> quIdList) {
		this.quIdList = quIdList;
	}



}
