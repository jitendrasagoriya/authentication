package com.js.authentication.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.js.authentication.model.DashBoard;

@Service
public interface DashBoardService {
	
	public Optional<DashBoard> getDashboard(String userId);
	public Optional<DashBoard> getDashboard();

}
