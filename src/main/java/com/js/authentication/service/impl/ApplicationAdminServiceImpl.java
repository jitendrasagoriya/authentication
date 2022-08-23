package com.js.authentication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.authentication.dao.ApplicationAdminDao;
import com.js.authentication.repository.ApplicationAdminRepository;
import com.js.authentication.service.ApplicationAdminService;

@Service
public class ApplicationAdminServiceImpl implements ApplicationAdminService {
	
	@Autowired
	private ApplicationAdminDao dao;

	@Override
	public ApplicationAdminRepository getRepository() {
		return dao.getRepository();
	}

}
