package com.cos.person.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	public List<User> fintAll(){
		List<User> users = new ArrayList<>();
		users.add(new User(1, "ssar", "1234", "0102222"));
		users.add(new User(2, "love", "1234", "0103333"));
		users.add(new User(3, "babo", "1234", "0104444"));
		return users;
	}
	public User fintById(int id) {
		return new User(1, "ssar", "1234", "0102222");
	}
	public void save(JoinReqDto dto) {
		System.out.println("DB에 insert");
	}
	public void delete(int id) {
		System.out.println("DB에 삭제하기");
	}
	public void update(int id, UpdateReqDto dto) {
		// DAO 연결해서 실행하다가 익섹션 터짐.
		throw new IllegalArgumentException("argument 잘못 넣음");
//		System.out.println("DB에 수정하기");
	}
}
