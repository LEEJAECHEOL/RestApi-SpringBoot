package com.cos.person.domain;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateReqDto {
	@NotNull(message = "패스워드를 입력하지 않았습니다.")
	private String password;
	private String phone;
}
