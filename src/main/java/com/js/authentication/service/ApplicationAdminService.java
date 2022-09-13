package com.js.authentication.service;

import com.js.authentication.dto.ApplicationUser;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.js.authentication.repository.ApplicationAdminRepository;

import java.util.List;
import java.util.Optional;

@Service
public interface ApplicationAdminService {
	public ApplicationAdminRepository getRepository();
	public List<ApplicationUser> getListOfAuthenticationWithApplicationNameByUserId(String userId);
	public List<Pair<Authentication, Application>> getListOfAuthenticationaWithApplicationByUserId(String userId);
	public Optional<ApplicationUser> getAuthenticationWithApplicationNameByUserId(String userId,String id);
}
