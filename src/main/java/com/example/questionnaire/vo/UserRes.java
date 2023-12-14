package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.User;

public class UserRes extends User {

	private RtnCode rtncode;

	private List<User> userList;

	public UserRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRes(RtnCode rtncode, List<User> userList) {
		super();
		this.rtncode = rtncode;
		this.userList = userList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public UserRes(RtnCode rtncode) {
		super();
		this.rtncode = rtncode;
	}

	public RtnCode getRtncode() {
		return rtncode;
	}

	public void setRtncode(RtnCode rtncode) {
		this.rtncode = rtncode;
	}

}
