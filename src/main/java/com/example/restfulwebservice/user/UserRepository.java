package com.example.restfulwebservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//database에 관련된 형태의 Bean이라고 알려준다.
//JpaRepository 인터페이스를 상속받는데 <Entity로 다룰 User 클래스 타입, 기본 데이터형을 지정해준다.)
public interface UserRepository extends JpaRepository<User, Integer> {

}
