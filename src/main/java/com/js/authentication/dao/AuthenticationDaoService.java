package com.js.authentication.dao;

import com.js.authentication.model.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
