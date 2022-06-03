package com.example.restfulwebservice.user;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//컨트롤러에서는 구현된 메서드를 사용한다.
@RestController
public class UserController {
	//UserDaoService클래스를 new 키워드가 아니라 메모리에 등록된 인스턴스 형식으로 사용한다. (의존성주입)
	//스프링부트에 의해 실행되는 즉시 메모리에 등록된 인스턴스 형식
	//스프링에서 관리되는 인스턴스를 Bean이라고 한다.(컨트롤러 Bean, 서비스 Bean...)
	//의존성 주입으로 관리한다.(setter나 생성자를 통해 의존성주입을 할 수 있다.)
	private UserDaoService service; // = new UserDaoService();
	
	//생성자로 의존성 주입하기
	//1.생성자에 매개변수로 인스턴스 객체를 선언해준다.
	//2.전달되어진 값 serivce은 인스턴스 변수 service에 담아준다.
	public UserController(UserDaoService service) {
		this.service = service;
	}
	
	@GetMapping("/users")
	//public List<User> retrieveAllUsers(){
	public MappingJacksonValue retrieveAllUsers(){
		//return service.findAll();
		List<User> users = service.findAll(); //리스트에 넣어주고 반환하도록 분리

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept("id", "name", "joinDate", "ssn"); //아이디 속성값

		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
		//UserInfo 이라고 되어있는 json filter가 적용된 User 도메인 필드를 사용

		MappingJacksonValue mapping = new MappingJacksonValue(users);
		mapping.setFilters(filters); //필터 적용

		return mapping;
	}

	//hateoas
	// 전체 사용자 목록
	@GetMapping("/users2")
	public ResponseEntity<CollectionModel<EntityModel<User>>> retrieveUserList2() {
		List<EntityModel<User>> result = new ArrayList<>();
		List<User> users = service.findAll();

		for (User user : users) {
			EntityModel entityModel = EntityModel.of(user);
			entityModel.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel());

			result.add(entityModel);
		}

		return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel()));
	}
	
	//GET /users/1 or /users/10
	//상세 조회하고 싶은 사용자
	@GetMapping("/users/{id}")
	//public User retrieveUser(@PathVariable int id) {
	//hateoas 적용으로 변경
	public ResponseEntity<EntityModel<User>> retrieveUser(@PathVariable int id) {
		
		User user = service.findOne(id);
		
		if (user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}
		//hateoas적용
		EntityModel entityModel = EntityModel.of(user);

		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(linkTo.withRel("all-users"));
		return ResponseEntity.ok(entityModel);		
		//return user; //hateoas적용으로 수정
	}
	/*
	* 여기서는 반환되는 부분에 filter 추가를 안했으므로
	* User.java에서 @JsonFilter("UserInfo") 이 부분을 주석처리하고
	* Get방식으로 http://localhost:8088/users/1 접근하면 다음과 같이 응답된다.
	* {
		"id": 1,
		"name": "kim",
		"joinDate": "2021-09-12T14:14:23.135+00:00",
		"password": "paas1",
		"ssn": "1234-1234",
		"_links": {
			"all-users": {
				"href": "http://localhost:8088/users"
			}
		}
	}
	* */
	
	//POST /users 도메인타입으로 전달받는다
	//사용자 추가하기
	//POST, PUT과 같은 HTML method에서 form 타입의 데이터가 아니라 -> json, xml 타입의 데이터를 받는 경우에는 매개변수에 RequestBody를 선언해주어야한다.
	//@RequestBody : 전달받은 데이터가 RequestBody의 역할을 하겠다는 의미
	//public void createUser(@RequestBody User user) {
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) { //@Valid 어노테이션만 추가 유효성 체크, 사용자 추가를 하는 createUser메서드에 적용
		//사용자 추가작업 post로 Body에서 "name"키에 해당하는 값으로 한글자만 넣어보자.
		//전달받은 데이터는 User Dao Service에 보내서 저장
		User savedUser = service.save(user);
		//추가
		//사용자한테 요청값을 반환해주기위해 ServletUriComponentsBuilder를 사용
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	/*
	 * 
	 * 결과값 확인 방법
	 * http://localhost:8088/users로 
	 	Postman에서 http method는 POST, Body에 raw, JSON(application/json) 타입으로 아래 내용을 직접 넣어서 전달
	 	{
    		"name": "wow",
    		"joinDate": "2021-04-12T16:34:05.499+00:00"
		}
		http://localhost:8088/users, GET방식으로 데이터가 추가되었음을 결과로 확인
	 * */
	
	//HTTP Method는 Delete
	@DeleteMapping("/users/{id}") //1. 이 url로 접근하면
	public void deleteUser(@PathVariable int id) {
		User user = service.deleteById(id); //2. 서비스 메서드 실행 삭제
		
		if(user == null) {
			//3. 삭제하려는 user가 없으면 예외 발생
			throw new UserNotFoundException(String.format("ID[%s] not found", id)); //예외 발생시 출력될 문구
		}
	}
}
