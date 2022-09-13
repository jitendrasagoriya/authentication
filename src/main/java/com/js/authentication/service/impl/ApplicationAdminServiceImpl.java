package com.js.authentication.service.impl;

import com.js.authentication.dto.ApplicationUser;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.js.authentication.dao.ApplicationAdminDao;
import com.js.authentication.repository.ApplicationAdminRepository;
import com.js.authentication.service.ApplicationAdminService;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationAdminServiceImpl implements ApplicationAdminService {
	
	@Autowired
	private ApplicationAdminDao dao;

	@Override
	public ApplicationAdminRepository getRepository() {
		return dao.getRepository();
	}

	@Override
	public List<ApplicationUser> getListOfAuthenticationWithApplicationNameByUserId(String userId) {
		return dao.getListOfAuthenticationWithApplicationNameByUserId(userId);
	}

	@Override
	public List<Pair<Authentication, Application>> getListOfAuthenticationaWithApplicationByUserId(String userId) {
		return dao.getListOfAuthenticationaWithApplicationByUserId(userId);
	}

	@Override
	public Optional<ApplicationUser> getAuthenticationWithApplicationNameByUserId(String userId, String id) {
		return dao.getAuthenticationWithApplicationNameByUserId(userId,id);
	}

}
