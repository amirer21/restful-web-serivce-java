package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.catalina.mapper.MapperListener;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/*
* 관리자 user controller
* */

//컨트롤러에서는 구현된 메서드를 사용한다.
@RestController
//공통적으로 가지고 있는 api이름이 있다면 클래스 블록에서 설정할 수 있다.
@RequestMapping("/admin") // 공통적인 이름(ex:/admin)이 있다면 클래스 블록에 이렇게 해두면 맨앞에는 기본값이 /admin/... 이런식이 된다.
public class AdminUserController {
	//UserDaoService클래스를 new 키워드가 아니라 메모리에 등록된 인스턴스 형식으로 사용한다. (의존성주입)
	//스프링부트에 의해 실행되는 즉시 메모리에 등록된 인스턴스 형식
	//스프링에서 관리되는 인스턴스를 Bean이라고 한다.(컨트롤러 Bean, 서비스 Bean...)
	//의존성 주입으로 관리한다.(setter나 생성자를 통해 의존성주입을 할 수 있다.)
	private UserDaoService service; // = new UserDaoService();

	//생성자로 의존성 주입하기
	//1.생성자에 매개변수로 인스턴스 객체를 선언해준다.
	//2.전달되어진 값 serivce은 인스턴스 변수 service에 담아준다.
	public AdminUserController(UserDaoService service) {
		this.service = service;
	}

	//다음주소로 테스트
	//강의에서는 /v1 부분이 없다.
	//http://localhost:8088/admin/users
	//http://localhost:8088/admin/v1/users/1

	@GetMapping("/users")
	//MappingJacksonValue라는 객체로 전달한다.
	//public List<User> retriveAllUsers(){
	// 필터링되어있는 객체를 반환할 때는 일반 List User도메인 객체를 반환할 수는 없고
	// MappingJacksonValue 객체로 변환시켜서 반환해야된다.
	public MappingJacksonValue retrieveAllUsers(){
		//return service.findAll();
		//코드 분리 Rafactor ->  'introduce variable'을 선택하면
		List<User> users = service.findAll(); //리스트에 넣어주고 반환하도록 분리

		//SimpleBeanPropertyFilter 클래스를 사용
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept("id", "name", "joinDate", "ssn"); //포함시키려는 필더값들 아이디 속성값
		//지정된 필드들만 JSON 변환한다. 알 수 없는 필드는 무시한다. 이 방식을 권장한다. 명백히 검증된 필드만 내보낸다.
		//집합에 포함된 속성을 제외한 모든 속성을 필터링하는 필터를 구성하는 팩토리 메서드

		//필터 이름 연결
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
		//필터를 사용하려면 어떠한 빈을 대상으로 사용하는지 넣어주어야한다. 빈의 이름은 User클래스에서 설정해둔 이름을(@JsonFilter("UserInfo"))
		//UserInfo 이라고 되어있는 json filter가 적용된 User 도메인 필드를 사용

		MappingJacksonValue mapping = new MappingJacksonValue(users);
		//필터 적용
		mapping.setFilters(filters);

		return mapping;
	}
	
	//GET /users/1 or /users/10
	//상세 조회하고 싶은 개별 사용자
	//mapping에 api 버전 부여하기
	@GetMapping("/v1/users/{id}")
	public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
		
		User user = service.findOne(id);
		
		if (user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept("id", "name", "joinDate", "ssn"); //아이디 속성값

		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

		MappingJacksonValue mapping = new MappingJacksonValue(user);
		mapping.setFilters(filters); //필터 적용
		
		return mapping;
	}

	@GetMapping("/v2/users/{id}")
	public MappingJacksonValue retrieveUserV2(@PathVariable int id) {

		User user = service.findOne(id);

		if (user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}

		// User -> User2 복사
		UserV2 userV2 = new UserV2();
		BeanUtils.copyProperties(user, userV2); //BeanUtils은 spring에서 Bean간 작업을 도와준다. id, name, joinDate등...
		userV2.setGrade("VIP"); //추가된 속성을 저장

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept("id", "name", "joinDate", "grade"); //아이디 속성값

		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);

		MappingJacksonValue mapping = new MappingJacksonValue(userV2);
		mapping.setFilters(filters); //필터 적용

		return mapping;
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	//Request Parameter와 Header를 이용한 API Version 관리
	//1.파라미터에 버전을 명시해서 구분하는 방법
	//http://localhost:8088/admin/users/1/?version=1
	@GetMapping(value = "/users/{id}/", params = "version=1")
	public MappingJacksonValue retrieveUserParamV1(@PathVariable int id) {

		User user = service.findOne(id);

		if (user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept("id", "name", "joinDate", "ssn"); //아이디 속성값

		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

		MappingJacksonValue mapping = new MappingJacksonValue(user);
		mapping.setFilters(filters); //필터 적용

		return mapping;
	}

	//2.header값으로 버전을 구분하는 방법
	// http://localhost:8088/admin/users/1
	// Postman에서 Headers에 Key : X-API-VERSION, Value : 1 를 넣고 Get방식으로 요청
	@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
	public MappingJacksonValue retrieveUserHeadersV1(@PathVariable int id) {

		User user = service.findOne(id);

		if (user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept("id", "name", "joinDate", "ssn"); //아이디 속성값

		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

		MappingJacksonValue mapping = new MappingJacksonValue(user);
		mapping.setFilters(filters); //필터 적용

		return mapping;
	}
	//////////////////////////////////////////////////////////////////////////////////////////

	//POST /users 도메인타입으로 전달받는다
	//사용자 추가하기
	//POST, PUT과 같은 HTML method에서 form 타입의 데이터가 아니라 -> json, xml 타입의 데이터를 받는 경우에는 매개변수에 RequestBody를 선언해주어야한다.
	//@RequestBody : 전달받은 데이터가 RequestBody의 역할을 하겠다는 의미
	//public void createUser(@RequestBody User user) {
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
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
