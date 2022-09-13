package com.js.authentication.service;

import org.springframework.stereotype.Service;

import com.js.authentication.enums.UserType;

@Service
public interface PermissionSerivce {

	public Boolean allow(UserType type);

}
