package com.js.authentication.dto;

import com.js.authentication.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class LoginResponce {
	private String token;
	private String userName;
	private String userId;
	private UserType userType;
}
