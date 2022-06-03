package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor //모든 args를 가지는 constructor
public class HelloWorldBeanVariable {
	private String messageName;

}
