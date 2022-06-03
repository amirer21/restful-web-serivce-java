package com.example.restfulwebservice.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

//비즈니스 로직은 서비스에 추가
//사용자 추가, 상세보기 등..

@Service
public class UserDaoService {
	private static List<User> users = new ArrayList<>();
	
	private static int usersCount = 3;

	//static 블럭의 변수에 초기값 생성
	//DB값
	static {
		//생성자의 매개변수
		users.add(new User(1, "kim", new Date(), "paas1", "1234-1234"));
		users.add(new User(2, "John", new Date(), "pass2", "2344-2344"));
		users.add(new User(3, "May", new Date(), "pass3", "4353-5555"));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	//사용자를 추가하는 데이터
	public User save(User user) {
		//기본 아이디가 없다면 지정
		if (user.getId() == null) {
			user.setId(++usersCount); //userCount는 위에 초기화한대로 3부터 시작
		}
		users.add(user);
		return user;
	}
	
	public User findOne(int id) {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	
	public User deleteById (int id) {
		//아이디 존재 여부 확인
		//List에서 하나씩 확인
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()) {
			User user = iterator.next();//하나씩 user에 포함
			if(user.getId() == id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}
}
