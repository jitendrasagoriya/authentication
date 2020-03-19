package com.js.authentication.service;

import com.js.authentication.model.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthenticationService {
    public boolean isValidUser(String accessToken);
    public Authentication getUserByToken(String accessToken);
    public Authentication getApplication(String email,  String password );
    public Authentication updateAuthentication(Authentication authentication);
    public Authentication saveAuthentication(Authentication authentication);
    public Boolean logOut(String accessToken);
    public List<Authentication> getByAppId(String appId);
    public Page<Authentication> getByAppId(String appId, Pageable pageable);
    public String generateAccessToken(String email,  String password ,String salt);
    public Authentication login(String accessToken);
    public boolean updateLastLogin(String accessToken);
}
