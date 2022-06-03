package com.example.restfulwebservice.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //모든 필드를 가지는 생성자
@NoArgsConstructor //매개변수가 없는 생성자
//예외에 대한 상세내용 보여주기
public class ExceptionResponse {
	private Date timestamp;
	private String message;
	private String details;
	
	/*
	 * http://localhost:8088/users/100 로 GET방식으로 접근해보면 아래와 같이 응답될 것이다.
	 * {
		    "timestamp": "2021-04-13T11:53:30.951+00:00",
		    "message": "ID[100] not found",
		    "details": "uri=/users/100"
		}
	 * */
}
