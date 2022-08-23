package com.js.authentication.service;

import org.springframework.stereotype.Service;

import com.js.authentication.repository.ApplicationAdminRepository;

@Service
public interface ApplicationAdminService {
	public ApplicationAdminRepository getRepository();
}
