package com.js.authentication.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.js.authentication.enums.UserType;
import com.js.authentication.model.Authentication;

@Service
public interface AuthenticationDaoService<R> extends BaseSerivce<R> {
    public boolean isValidUser(String accessToken);
    public Authentication getUserByToken(String accessToken);
    public Authentication getApplication(String email,  String password, UserType type );
    public Authentication saveAuthentication(Authentication authentication);
    public Boolean logOut(String accessToken);
    public Boolean updatePassword(String accessToken);
    public List<Authentication> byAppId(String appId);
    public Page<Authentication> byAppId(String appId, Pageable pageable);
    public boolean updateLastLogin(String accessToken);
    public Boolean deleteByToken(String accessToken);
    public Boolean deleteById(String id);
    public Page<Authentication> getAll( Pageable pageable);
    public Page<Authentication> getAll( Example<Authentication> search, Pageable pageable);
    public Optional<Authentication> getUserByEmail(String email);
    public Authentication save(Authentication authentication);
    public Authentication findByVerificationCode(String verificationCode);
    public Optional<Authentication> getUserByEmail(String email,UserType userType);
	Optional<Authentication> getAuthentication(String email, String password, UserType type, String appId);
	Optional<Authentication> getUserByEmail(String email, UserType userType, String appId);
    public Optional<Authentication> getAuthenticationById(String id);
    Boolean existsById(String id);
    Boolean existsById(Example<Authentication> example);
}
