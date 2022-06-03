package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//HTTP Status code
//2XX -> ok
//4XX -> client error
//5XX -> Server error

//이 예외클래스는 서버 에러 5XX번대가 아니라 클라이언트 에러 4XX대로 NOT_FOUND로 전달
//"데이터가 존재하지 않습니다." 라는 메시지전달
// 이 어노테이션이 없다면 응답결과로 "status": 500,"error": "Internal Server Error",
// 이 어노테이션이 있다면 응답결과로     "status": 404,"error": "Not Found",
@ResponseStatus(HttpStatus.NOT_FOUND) 
public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message){
		super(message);
	}
}
