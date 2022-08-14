package com.js.authentication.service;

import com.js.authentication.exception.NoSuchUserFound;
import com.js.authentication.model.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthenticationService {
    public boolean isValidUser(String accessToken);
    public Optional<Authentication> getUserByToken(String accessToken);
    public Authentication getApplication(String email,  String password );
    public Authentication updateAuthentication(Authentication authentication);
    public Authentication saveAuthentication(Authentication authentication);
    public Boolean logOut(String accessToken);
    public List<Authentication> getByAppId(String appId);
    public Page<Authentication> getByAppId(String appId, Pageable pageable);
    public Optional<String> generateAccessToken(String email,  String password ,String salt) throws NoSuchUserFound;
    public Authentication login(String accessToken);
    public boolean updateLastLogin(String accessToken);
    public Boolean deleteByToken(String accessToken);

    public Page<Authentication> getAll ( Pageable pageable);
    public Optional<Authentication> getUserByEmail(String email);
    public Authentication save(Authentication authentication);
    public boolean verify(String verificationCode);
}
