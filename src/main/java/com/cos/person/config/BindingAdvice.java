package com.cos.person.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cos.person.domain.CommonDto;

// 공통기능들을 advice라고 부른다.

@Component
@Aspect
public class BindingAdvice  {
	
	
	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);

	
	// 어떤 함수가 언제 몇번 실행됐는지 횟수같은거 로그 남기기
	// 그메소드가 실행되기 전이기 때문에 ProceedingJoinPoint를 사용할 필요없다.
	@Before("execution(* com.cos.person.web..*Controller.*(..))")
	public void testCheck() {
		// request 값 처리
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println("주소 : " + request.getRequestURI());
		
		
		System.out.println("전처리 로그를 남김");
	}
	
	//  그메소드가 실행되기 전이기 때문에 ProceedingJoinPoint를 사용할 필요없다.
	@After("execution(* com.cos.person.web..*Controller.*(..))")
	public void testCheck2() {
		System.out.println("후처리 로그를 남김");
	}
	
	// 앞만 제어하면 리턴을 못함 그래서 앞뒤로 제어해야된다.
	// 함수 : 앞 뒤
	// 함수 : 앞 (유저네임이 안들어왔으면 내가 강제로 넣어주고 실행)
	// 함수 : 뒤 (응답만 관리)
	
//	@Before	//앞만 제어
//	@After	//뒤만 제어
	// ProceedingJoinPoint는 around만 가질수 있다.
	@Around("execution(* com.cos.person.web..*Controller.*(..))")	// 앞뒤 제어
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		System.out.println("type : " + type);
		System.out.println("method : " + method);
		
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;
				// 서비스 : 정상적인 화면 -> 사용자요청
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						// 로그 레벨 error -> warn -> info -> debug
						log.warn(type + "." + method + "() => 필드 : " + error.getField() + ", 메시지 : " + error.getDefaultMessage());
						
					}
					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라.
	}
}
