package com.example.restfulwebservice.user;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * User 도메인 클래스
 */
@Data
@AllArgsConstructor //전체 필드를 가지는 생성자
//@JsonIgnoreProperties(value={"password", "ssn"}) //value 속성에 제어하려는 데이터의 필드명을 넣어준다. 사용자에게 보여주지 않으려는 데이터를 정의한다.
//@JsonFilter("UserInfo") //이름은 임의로 부여한다.
@NoArgsConstructor //기본 생성자 어노테이션 : public User(){}
@ApiModel(description = "사용자 상제 정보를 위한 도메인 객체")
@Entity//프로젝트를 실행하면 해당 클래스의 이름으로 데이터베이스 테이블을 생성한다. 필드로 테이블의 컬럼을 생성한다.
//build하고 로그에서 'create'라고 검색해서 확인해보자
//create table user (id integer not null, join_date timestamp, name varchar(255), password varchar(255), ssn varchar(255), primary key (id))
public class User {
	@Id//기본키값
	@GeneratedValue//자동생성
	private Integer id;
	//글자는 최소 2자이상으로하는 제약조건
	@Size(min=2, message = "Name은 2글자 이상 입력해 주세요.")
	@ApiModelProperty(notes = "사용자 이름을 입력해 주세요.")
	private String name;
	//시간은 과거 시간만 사용하도록하는 제약조건
	@Past
	@ApiModelProperty(notes = "사용자 등록일을 입력해 주세요.")
	private Date joinDate;

	//전달하려는 데이터를 노출하지 않도록 제어한다.
	//@JsonIgnore
	@ApiModelProperty(notes = "사용자 패스워드를 입력해 주세요.")
	private String password;
	//@JsonIgnore
	@ApiModelProperty(notes = "사용자 주민번호를 입력해 주세요.")
	private String ssn;

	@OneToMany(mappedBy = "user")//user테이블과 맵핑, 1:다
	private List<Post> posts;
	//생성자
	public User(int id, String name, Date joinDate, String password, String ssn){
		this.id = id;
		this.name = name;
		this.joinDate =joinDate;
		this.password = password;
		this.ssn = ssn;
	}

}
