package com.js.authentication.dao.impl;

import com.js.authentication.dao.AuthenticationDaoService;
import com.js.authentication.model.Authentication;
import com.js.authentication.repository.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationDaoServiceImpl implements AuthenticationDaoService<AuthenticationRepository> {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationDaoServiceImpl.class);

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public boolean isValidUser(String accessToken) {
        Authentication appAuthentication = authenticationRepository.getActive(accessToken);
        if(appAuthentication!=null)
            return true;
        return false;
    }

    @Override
    public Authentication getUserByToken(String accessToken) {
        Authentication app = authenticationRepository.getActive(accessToken);
        if(app!=null)
            return app;
        return null;
    }

    @Override
    public Authentication getApplication(String email, String password) {
        return authenticationRepository.getApplication(email,password);
    }

    @Override
    public Authentication saveAuthentication(Authentication authentication) {
        return authenticationRepository.save(authentication);
    }

    @Override
    public Boolean logOut(String accessToken) {
        return authenticationRepository.logout(accessToken)>0?Boolean.TRUE:Boolean.FALSE;
    }

    @Override
    public Boolean updatePassword(String accessToken) {
        return null;
    }

    @Override
    public List<Authentication> byAppId(String appId) {
        return authenticationRepository.getAuthenticationByAppId(appId);
    }

    @Override
    public Page<Authentication> byAppId(String appId, Pageable pageable) {
        return authenticationRepository.getAuthenticationByAppId(appId,pageable);
    }

    @Override
    @Transactional
    public boolean updateLastLogin(String accessToken) {
        return authenticationRepository.updateLoginTimestamp(accessToken)<0?Boolean.TRUE:Boolean.FALSE;
    }

    @Override
    @Transactional
    public Boolean deleteByToken(String accessToken) {
        return authenticationRepository.deleteByToken(accessToken) > 0?Boolean.TRUE:Boolean.FALSE;
    }

    @Override
    public Page<Authentication> getAll(Pageable pageable) {
        return authenticationRepository.findAll(pageable);
    }

    @Override
    public AuthenticationRepository getRepository() {
        return authenticationRepository;
    }

	@Override
	public Optional<Authentication> getUserByEmail(String email) { 
		Authentication authentication = authenticationRepository.getAuthentication(email);
		if(authentication==null)
			return Optional.empty();
		return Optional.of(authentication);
	}

	@Override
	public Authentication save(Authentication authentication) {
		return authenticationRepository.saveAndFlush(authentication);
	}

	@Override
	public Authentication findByVerificationCode(String verificationCode) { 
		return authenticationRepository.findByVerificationCode(verificationCode);
	}
}
