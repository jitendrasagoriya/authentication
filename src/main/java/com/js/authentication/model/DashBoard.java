package com.js.authentication.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.js.authentication.dto.Data;
import com.js.authentication.model.Authentication.AuthenticationBuilder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class DashBoard {

	private Map<Application, List<Optional<Authentication>>> applications;

	public DashBoard() {
		this.applications = new HashMap<>();
	}

	public int totalApplication() {
		return applications.keySet().size();
	}

	public long totalAuthentication() {
		return applications.values().stream().flatMap(l -> l.stream()).count();
	}

	public long totalNewUsers() {
		return applications.values().stream().flatMap(l -> l.stream())
				.filter(a -> !a.orElse(new AuthenticationBuilder().isActive(false).build()).getIsActive()).count();
	}

	public long totalLoggedInUsers() {
		return applications.values().stream().flatMap(l -> l.stream())
				.filter(a -> !a.orElse(new AuthenticationBuilder().logout(true).build()).getIsActive()).count();
	}

	public List<Optional<Authentication>> newUsers() {
		return applications.values().stream().flatMap(l -> l.stream())
				.filter(a -> !a.orElse(new AuthenticationBuilder().isActive(false).build()).getIsActive())
				.collect(Collectors.toList());
	}

	public List<Optional<Authentication>> totalUserList() {
		return applications.values().stream().flatMap(l -> l.stream()).collect(Collectors.toList());
	}

	public List<Optional<Data>> getData() {
		List<Optional<Data>> list = new ArrayList<>();
		applications.entrySet().forEach(e -> {
			list.add(Optional.of(new Data(e.getKey(), e.getValue())));
		});
		return list;

	}
}
