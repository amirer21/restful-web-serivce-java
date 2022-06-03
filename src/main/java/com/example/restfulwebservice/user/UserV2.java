package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password", "ssn"}) //value 속성에 제어하려는 데이터의 필드명을 넣어준다.
@JsonFilter("UserInfoV2") //이름은 임의로 부여한다.
//상속받는 부모클래스에 default 생성자가 존재하지 않는다면 오류가 발생된다.
//There is no default constructor available in 'com.example.restfulwebservice.user.User'
//부모클래스에 어노테이션을 넣어주거나 기본 생성자를 만들어준다. -> @NoArgsConstructor //기본 생성자 어노테이션 : public User(){}
public class UserV2 extends User { //User를 상속받는다.

	public UserV2(){} //기본 생성자이다. @NoArgsConstructor를 붙여두어도된다.
	private String grade;

}
