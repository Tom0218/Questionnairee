package com.example.questionnaire.constants;

public enum RtnCode {

	SUCCESSFUL(200, "Successful!"), //
	QUESTION_PARAM_ERROR(400,"Question param error!"),//
	QUESTIONNAIRE_PARAM_ERROR(400,"Questionnaire param error!"),//
	QUESTIONNAIRE_ID_PARAM_ERROR(400,"Questionnaire id param error!"),//
	QUESTIONNAIRE_ID_NOT_FOUND(404,"Questionnaire id not found!"),//
	UPDATE_ERROR(400,"Update error!"),
	CAN_NOT_BE_EMPTY(400,"Can not be empty!!"),
	SUBMISSION_IS_NOT_FOUND(400,"Submission is nor found!!"),
	;
	

	private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
