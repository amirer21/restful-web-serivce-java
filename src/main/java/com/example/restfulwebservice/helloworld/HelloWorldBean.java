package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
/* lombok 플러그인은 아래와 같은 getter, setter를 자동으로 완성해준다.*/

@Data
@AllArgsConstructor //모든 args를 가지는 constructor
public class HelloWorldBean {
	private String messageName;
	private String messageName2;
	private String messageName3;

	/*
	//위에 lombok.Data를 사용하면 getter, setter 생략이 가능해진다.
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage() {
		this.message = message;
	}*/

	//모든 args를 가지는 constructor
	/*
	public HelloWorldBean(String message) {
		this.message = message;
	}*/

}
