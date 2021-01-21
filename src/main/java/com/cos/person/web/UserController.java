package com.cos.person.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.person.domain.CommonDto;
import com.cos.person.domain.JoinReqDto;
import com.cos.person.domain.UpdateReqDto;
import com.cos.person.domain.User;
import com.cos.person.domain.UserRepository;

@RestController
public class UserController {
	
	private UserRepository userRepository;
	
	// DI = 의존성 주입
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// http://localhost:8000/user
	@GetMapping("/user")
	public CommonDto<List<User>> findAll() {
		System.out.println("fintAll()");
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.fintAll());// MessageConverter 가 매핑해줌.(JavaObject -> Json)
		
	}
	
	// http://localhost:8000/user/1
	@GetMapping("/user/{id}")
	public CommonDto<User> findById(@PathVariable int id) {
		System.out.println("findById() : id : " + id);
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.fintById(id));
	}
	
	@CrossOrigin 	// 외부에서도 접근가능하
	// cors정책 = 외부에서 서버로 ajax로 장난질하는거를 막으려고 외부에서 들어오는 자바스크립트를 다막은거(자바스크립트로 http못하게 막은거)
	// cors풀어주려면 @CrossOrigin을 해당하는 매핑위에 걸어준다.
	// 스프링부트의 좋은점 = Controller가 파싱도안하고 데이터받고 분기도 안시키고 바로 레파지토리쪽으로 데이터 보내고 응답만 리턴해줌.
	// exception같은건 핸들러가 다 낚아챔.(Dao에서)
	// http://localhost:8000/user
	@PostMapping("/user")
	// x-www-form-urlencoded => (request.getParameter())파싱해서 매핑해서 넣어줌
	// text/plain => @RequestBody 어노테이션 를 통해서 값을 받아옴
	// (클라이언트가 요청 할 때) application/json => @ResponseBody 어노테이션 + 오브젝트로 받기 
	public CommonDto<?> save(@Valid @RequestBody JoinReqDto dto, BindingResult bindingResult) {
		// 제너릭을 무슨 타입을 사용할 지 몰라서 ?를 사용한다.
//		if(bindingResult.hasErrors()) {
//			Map<String, String> errorMap = new HashMap<>();
//			for(FieldError error : bindingResult.getFieldErrors()) {
//				errorMap.put(error.getField(), error.getDefaultMessage());
//			}
//			return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
//		}
//		
		System.out.println("save()");
		System.out.println("user : " + dto);
		userRepository.save(dto);
		
//		System.out.println("data :" + data);
//		System.out.println("username : " + username);
//		System.out.println("password : " + password);
//		System.out.println("phone : " + phone);
		
		return new CommonDto<>(HttpStatus.CREATED.value(), "ok");
	}
	
	// http://localhost:8000/user/1
	@DeleteMapping("/user/{id}")
	public CommonDto<String> delete(@PathVariable int id) {
		System.out.println("delete() - id : " + id);
		userRepository.delete(id);
		return new CommonDto<>(HttpStatus.OK.value(), null);
	}
	
	// http://localhost:8000/user/1
	@PutMapping("/user/{id}")
	public CommonDto<?> update(@Valid @PathVariable int id, @RequestBody UpdateReqDto dto, BindingResult bindingResult) {
		// 공통 기능 -> 필터만들어서 미리 처리 : 전처리만 가능 후처리를 만들어서 굳이 내가 할 필요없음
		// AoP를 사용할거임
//		if(bindingResult.hasErrors()) {
//			Map<String, String> errorMap = new HashMap<>();
//			for(FieldError error : bindingResult.getFieldErrors()) {
//				errorMap.put(error.getField(), error.getDefaultMessage());
//			}
//			return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
//		}
		System.out.println("update() dto : " + dto);
		userRepository.update(id, dto);
		return new CommonDto<>(HttpStatus.OK.value(), null);
	}
}
