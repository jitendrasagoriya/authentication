package com.js.authentication.dto;

import java.util.List;
import java.util.Optional;

import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Data {

	private Application application;
	private List<Optional<Authentication>> authentication;
}
