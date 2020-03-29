package com.js.authentication.service.impl;

import com.js.authentication.dao.AuthenticationDaoService;
import com.js.authentication.dao.impl.AuthenticationDaoServiceImpl;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.AuthenticationService;
import com.js.authentication.token.SecureTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private AuthenticationDaoServiceImpl daoService;

    @Override
    public boolean isValidUser(String accessToken) {
        return daoService.isValidUser(accessToken);
    }

    @Override
    public Authentication getUserByToken(String accessToken) {
        return daoService.getUserByToken(accessToken);
    }

    @Override
    public Authentication getApplication(String email, String password) {
        return daoService.getApplication(email,password);
    }

    @Override
    public Authentication updateAuthentication(Authentication authentication) {
        try {
                return daoService.saveAuthentication(authentication);
        } catch (Exception e){
            logger.error("Error during update Authentication",e);
        }
        return null;
    }

    @Override
    public Authentication saveAuthentication(Authentication authentication) {
        authentication.setCreationDate(new Timestamp(System.currentTimeMillis()));
        authentication.setExpaireDay(10);
        authentication.setIsActive(Boolean.TRUE);
        authentication.setIsLogout(Boolean.TRUE);
        authentication.setUserId(SecureTokenGenerator.getUserId(10));
        return daoService.saveAuthentication(authentication);
    }

    @Override
    public Boolean logOut(String accessToken) {
        return daoService.logOut(accessToken);
    }

    @Override
    public List<Authentication> getByAppId(String appId) {
        return daoService.byAppId(appId);
    }

    @Override
    public Page<Authentication> getByAppId(String appId, Pageable pageable) {
        return daoService.byAppId(appId,pageable);
    }

    @Override
    public String generateAccessToken(String email, String password,String salt) {
        Authentication authentication = getApplication(email, PasswordUtils.generateSecurePassword( password,salt) );
        String assessToken = SecureTokenGenerator.nextAppId(authentication.getAppId());
        authentication.setToken(assessToken);
        authentication.setIsLogout(false);
        authentication = updateAuthentication(authentication);
        return authentication.getToken();
    }

    @Override
    public Authentication login(String accessToken) {

        Authentication authentication = daoService.getUserByToken(accessToken);
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication fail : Access Token :"
                    + accessToken);
        }

        if(authentication != null) {
            //UPDATE LAST LOGIN DATE
            updateLastLogin(accessToken);
        }
        return authentication;
    }

    @Override
    public boolean updateLastLogin(String accessToken) {
        return daoService.updateLastLogin(accessToken);
    }

    @Override
    public Boolean deleteByToken(String accessToken) {
        return daoService.deleteByToken(accessToken);
    }

    @Override
    public Page<Authentication> getAll(Pageable pageable) {
        return daoService.getAll(pageable);
    }
}
