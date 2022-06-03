package com.example.restfulwebservice.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.restfulwebservice.user.UserNotFoundException;

@RestController
@ControllerAdvice//@ControllerAdvice : 모든 컨트롤러가 실행될때 반드시 이 어노테이션을 가지는 Bean(메서드)이 사전에 실행된다. 그래서 예외 발생시 이 클래스가 실행될 것이다.
//AOP 컨트롤러에서 공통적으로 처리되어야 하는 로직, 메서드를 여기에 추가해준다
//ResponseEntityExceptionHandler를 상속받아 예외 에러를 핸들링하는 클래스
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	//ResponseEntity : 사용자 객체를 한명 추가했을때 반환되는 값
	//handleAllException(에러객체, 어디서 발생된 것인지)
	@ExceptionHandler(Exception.class)
	//handleAllException 모든 예외 처리
	//에러 객체, 어디서 발생했는지
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);//500로 응답된다.
	}


	//handleUserNotFoundException 위에는 모든 예외처리이지만 여기는 '유저가 없는 경우'에 처리되는 메서드를 만들어보자
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND); //Not found, 404로 응답된다.
	}


	//현재 상속받는 ResponseEntityExceptionHandler 클래스의  handleMethodArgumentNotValid 메서드를 오버라이드한다.
	//오버라이드 하는경우 오버라이드라고 표시한다.
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), ex.getBindingResult().toString());
		//return handleExceptionInternal(ex, null, headers, status, request);
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

}