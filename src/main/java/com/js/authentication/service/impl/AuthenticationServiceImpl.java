package com.js.authentication.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.js.authentication.dao.impl.AuthenticationDaoServiceImpl;
import com.js.authentication.enums.UserType;
import com.js.authentication.exception.NoSuchUserFound;
import com.js.authentication.exception.UserNotRegisteredWithApplication;
import com.js.authentication.exception.UserNotVerified;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.AuthenticationService;
import com.js.authentication.token.SecureTokenGenerator;

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
	public Optional<Authentication> getUserByToken(String accessToken) {
		Authentication authentication = daoService.getUserByToken(accessToken);
		if (authentication == null)
			return Optional.empty();
		return Optional.of(authentication);
	}

	@Override
	public Authentication getApplication(String email, String password, UserType type) {
		return daoService.getApplication(email, password, type);
	}

	@Override
	public Authentication updateAuthentication(Authentication authentication) {
		try {
			return daoService.saveAuthentication(authentication);
		} catch (Exception e) {
			logger.error("Error during update Authentication", e);
		}
		return null;
	}

	@Override
	public Authentication saveAuthentication(Authentication authentication) {
		authentication.setCreationDate(new Timestamp(System.currentTimeMillis()));
		authentication.setExpaireDay(10);
		authentication.setIsActive(Boolean.FALSE);
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
		return daoService.byAppId(appId, pageable);
	}

	@Override
	public Optional<String> generateAccessToken(String email, String password, String salt, UserType userType,
			String appId) throws NoSuchUserFound, UserNotVerified, UserNotRegisteredWithApplication {

		Optional<Authentication> optAuthentication = getAuthentication(email,
				PasswordUtils.generateSecurePassword(password, salt), userType, appId);

		if (!optAuthentication.isPresent()) {
			throw new NoSuchUserFound(email);
		}

		Authentication authentication = optAuthentication.get();

		if (!StringUtils.equals(appId, authentication.getAppId()))
			throw new UserNotRegisteredWithApplication();

		if (!authentication.getIsActive()) {
			throw new UserNotVerified();
		}
		String assessToken = SecureTokenGenerator.getToken();
		authentication.setToken(assessToken);
		authentication.setIsLogout(false);
		authentication = updateAuthentication(authentication);

		if (authentication == null)
			return Optional.empty();

		if (authentication.getUserType().getId() != userType.getId())
			return Optional.empty();

		return Optional.of(authentication.getToken());
	}

	public boolean verify(String verificationCode) {
		Authentication authentication = daoService.findByVerificationCode(verificationCode);

		if (authentication == null || authentication.getIsActive()) {
			return false;
		} else {
			authentication.setVerificationCode(null);
			authentication.setIsActive(Boolean.TRUE);
			daoService.save(authentication);
			return true;
		}

	}

	@Override
	public Authentication login(String accessToken) {

		Authentication authentication = daoService.getUserByToken(accessToken);
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication fail : Access Token :" + accessToken);
		}

		if (authentication != null) {
			// UPDATE LAST LOGIN DATE
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
	public Boolean deleteById(String id) {
		return daoService.deleteById(id);
	}

	@Override
	public Page<Authentication> getAll(Pageable pageable) {
		return daoService.getAll(pageable);
	}

	@Override
	public Optional<Authentication> getUserByEmail(String email) {
		return daoService.getUserByEmail(email);
	}

	@Override
	public Authentication save(Authentication authentication) {
		return daoService.save(authentication);
	}

	@Override
	public Optional<Authentication> getUserByEmail(String email, UserType userType) {
		return daoService.getUserByEmail(email, userType);
	}

	@Override
	public Authentication getAuthentication(String email, String password, UserType userType) {
		return daoService.getApplication(email, password, userType);
	}

	@Override
	public Authentication getApplication(String email, String password) {
		return null;
	}

	@Override
	public Optional<Authentication> getAuthentication(String email, String password, UserType type, String appId) {
		return daoService.getAuthentication(email, password, type, appId);
	}

	@Override
	public Optional<Authentication> getUserByEmail(String email, UserType userType, String appId) {
		return daoService.getUserByEmail(email, userType, appId);
	}

	@Override
	public Optional<Authentication> getAuthenticationById(String id) {
		return daoService.getAuthenticationById(id);
	}

	@Override
	public Boolean existsById(String id) {
		return daoService.existsById(id);
	}

	@Override
	public Boolean existsById(Authentication authentication) {
		Example<Authentication> search = Example.of(authentication);
		return daoService.existsById(search);
	}

	@Override
	public Page<Authentication> getAll(Authentication authentication, Pageable pageable) {	
		Example<Authentication> search = Example.of(authentication);
		return daoService.getAll(search, pageable);
	}
}
