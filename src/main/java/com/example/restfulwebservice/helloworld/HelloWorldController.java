package com.example.restfulwebservice.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorldController {

	//어노테이션을 사용한 의존성 주입
	//현재 스프링 프레임워크에 등록된 Bean중에서 같은 타입을 가진 Bean을 자동으로 주입
	@Autowired
	private MessageSource messageSource;//message값을 반환시켜주기위해 클래스의 인스턴스 생성


	String hello3;
	//GET
	//url는  hello-world (endpoint)
	//@RequestMapping(method =Request.GET, path="/hello-world" 대신 GetMapping 사용
	@GetMapping(path="/hello-world")
	public String helloWorld() {
		return "hello world";
	}
	
	@GetMapping(path="/hello-world-bean")
	//새로만든 데이터형으로 반환하게 되면 spring framework에서는 json형태로 반환한다
	//rest controller를 사용하면 자동으로 json 형태로 반환된다.
	public HelloWorldBean helloWorldBean() {
		System.out.println("/hello-world-bean 요청");
		String hello2 = "hello world2";
		hello3();
		System.out.println("변수 hello3 에 저장된 값은? >>> "+ hello3);
		return new HelloWorldBean("hello world", hello2, hello3);
	}
	
	public String hello3() {
		return this.hello3 = "hello world3";		
	}
	
	@GetMapping(path="/hello-world-bean/path-variable/{name}")
	//새로만든 데이터형으로 반환하게 되면 spring framework에서는 json형태로 반환한다
	//rest controller를 사용하면 자동으로 json 형태로 반환된다.
	public HelloWorldBeanVariable helloWorldBeanVarible(@PathVariable String name) {
		System.out.println("/hello-world-bean/variable 요청");
		System.out.println("변수 name 에 저장된 값은? >>> "+ name);
		return new HelloWorldBeanVariable(String.format("hello world %s", name));
	}

	/*
	* localhost:8088/hello-world-internationalized 주소로 get방식으로 요청
	* Headers의 Key에 Accept-Language, Value에 fr 을 넣어주면 "bojour" 문자열이 호출된다.
	* */
	@GetMapping(path = "/hello-world-internationalized")
	public String helloWorldInternationalized(@RequestHeader(name="Accept-Language", required = false) Locale locale){
		return messageSource.getMessage("greeting.message", null, locale);
	}
}
