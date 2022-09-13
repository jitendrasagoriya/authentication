package com.js.authentication.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import com.js.authentication.model.DashBoard;
import com.js.authentication.service.CommonService;
import com.js.authentication.service.DashBoardService;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Autowired
	private CommonService service;

	@Override
	public Optional<DashBoard> getDashboard(String userId) {
		Map<Application, List<Optional<Authentication>>> result = new HashMap<>();
		List<Application> applications = null;
		try {
			if (userId != null)
				applications = service.getApplicationService().getApplicationByUserId(userId);
			else
				applications = service.getApplicationService().getAll();

			for (Application application : applications) {
				List<Optional<Authentication>> authentications = service.getAuthenticationService()
						.getByAppId(application.getId()).stream()
						.map(i -> new Authentication.AuthenticationBuilder(i).token("").passward("").build())
						.map(i -> Optional.of(i)).collect(Collectors.toList());
				result.put(application, authentications);
			}
			return Optional.of(new DashBoard(result));
		} catch (Exception e) { 
			return Optional.empty();
		}

	}

	@Override
	public Optional<DashBoard> getDashboard() {
		return getDashboard(null);
	}

}
