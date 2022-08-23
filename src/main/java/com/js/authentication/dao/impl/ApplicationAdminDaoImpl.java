package com.js.authentication.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.authentication.dao.ApplicationAdminDao;
import com.js.authentication.repository.ApplicationAdminRepository;

@Service
public class ApplicationAdminDaoImpl implements ApplicationAdminDao {
	
	@Autowired
	private ApplicationAdminRepository repository;

	@Override
	public ApplicationAdminRepository getRepository() {		 
		return repository;
	}

}
