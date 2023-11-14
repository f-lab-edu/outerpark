package com.sadowbass.outerpark.application.account.exception;

public class AlreadyLoggedInException extends RuntimeException{

	public AlreadyLoggedInException() {
		super("이미 로그인 된 사용자 입니다");
	}
}
