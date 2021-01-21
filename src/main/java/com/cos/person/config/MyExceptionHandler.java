package com.cos.person.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Exception 을 낚아 채기
@RestController
@RestControllerAdvice
public class MyExceptionHandler {
	@ExceptionHandler(value=IllegalArgumentException.class)
	public String argumentException(IllegalArgumentException e) {
		return "오류 : " + e.getMessage();
	}
}
