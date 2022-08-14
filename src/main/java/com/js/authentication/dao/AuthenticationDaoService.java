package com.js.authentication.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.js.authentication.model.Authentication;

@Service
public interface AuthenticationDaoService<R> extends BaseSerivce<R> {
    public boolean isValidUser(String accessToken);
    public Authentication getUserByToken(String accessToken);
    public Authentication getApplication(String email,  String password );
    public Authentication saveAuthentication(Authentication authentication);
    public Boolean logOut(String accessToken);
    public Boolean updatePassword(String accessToken);
    public List<Authentication> byAppId(String appId);
    public Page<Authentication> byAppId(String appId, Pageable pageable);
    public boolean updateLastLogin(String accessToken);
    public Boolean deleteByToken(String accessToken);
    public Page<Authentication> getAll( Pageable pageable);
    public Optional<Authentication> getUserByEmail(String email);
    public Authentication save(Authentication authentication);
    public Authentication findByVerificationCode(String verificationCode);
}
