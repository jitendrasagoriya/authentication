package com.js.authentication.endpoint;

import com.js.authentication.dto.ChangePassword;
import com.js.authentication.dto.LoginDto;
import com.js.authentication.exception.ApplicationNotRegistered;
import com.js.authentication.exception.InvalidUserNameAndPassword;
import com.js.authentication.exception.NoSuchUserFound;
import com.js.authentication.exception.UserAlreadyExixts;
import com.js.authentication.exception.UserNotVerified;
import com.js.authentication.mail.service.impl.EmailServiceImpl;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.CommonService;

import net.bytebuddy.utility.RandomString;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/authentication/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AuthenticationEndpoint {

	private final Logger logger = LoggerFactory.getLogger(ApplicationEndpoint.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private EmailServiceImpl emailService;

	@PostMapping(path = "logout/", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> logout(@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
			@RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access) {
		Boolean isLogout = false;
		try {
			Application application = commonService.getApplicationService().getByAppAccess(appAccess);
			if (application == null) {
				logger.error("Application is not registered with us");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}
			isLogout = commonService.getAuthenticationService().logOut(access);
			return new ResponseEntity<>(isLogout, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(path = "auth/", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAuthentication(@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
			@RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access) {
		try {

			Application application = commonService.getApplicationService().getByAppAccess(appAccess);
			if (application == null) {
				logger.error("Application is not registered with us");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}

			Authentication authentication = null;

			authentication = commonService.getAuthenticationService().login(access);

			if (authentication != null) {
				if (!authentication.getIsActive()) {
					logger.error("User is not verified.");
					return new ResponseEntity<Exception>(new UserNotVerified("User is not verified."), HttpStatus.OK);
				}
			}

			Authentication responce = new Authentication.AuthenticationBuilder(authentication).build();
			responce.setToken("");
			responce.setPassward("");

			return new ResponseEntity<>(authentication, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(path = "token/{appid}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAccessToken(@PathVariable("appid") String appid,
			@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String access, @RequestBody LoginDto loginDto) {
		if (logger.isDebugEnabled())
			logger.debug("Fetching access token..");
		try {
			Application application = commonService.getApplicationService().getByAppIdAndAccess(appid, access);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appid);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(appid), HttpStatus.UNAUTHORIZED);
			}

			Optional<String> accessToken = commonService.getAuthenticationService()
					.generateAccessToken(loginDto.getUserName(), loginDto.getPassword(), application.getSalt());
 
			return new ResponseEntity<String>(accessToken.orElseThrow(() -> new InvalidUserNameAndPassword()), HttpStatus.OK);
		} catch (UserNotVerified e) {
			return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchUserFound noSuchUserFound) {
			return new ResponseEntity<>(new InvalidUserNameAndPassword(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}

	}

	@PostMapping(path = "changePassword/", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changePassword(@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
			@RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access, @RequestBody ChangePassword request) {
		if (logger.isDebugEnabled())
			logger.debug("Fetching access token..");
		try {
			Application application = commonService.getApplicationService().getByAppAccess(appAccess);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appAccess);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}
			Optional<Authentication> auth = commonService.getAuthenticationService().getUserByToken(access);

			if (!auth.isPresent())
				return new ResponseEntity<String>("Invalid access token. Please login again.", HttpStatus.UNAUTHORIZED);

			String currentPassword = PasswordUtils.generateSecurePassword(request.getOldPassword(),
					application.getSalt());

			if (!StringUtils.equals(auth.get().getPassward(), currentPassword))
				return new ResponseEntity<String>("Incorrect current password.", HttpStatus.BAD_REQUEST);

			Authentication authentication = auth.get();
			authentication
					.setPassward(PasswordUtils.generateSecurePassword(request.getNewPassword(), application.getSalt()));

			commonService.getAuthenticationService().save(authentication);

			return new ResponseEntity<Authentication>(
					new Authentication.AuthenticationBuilder().userName(authentication.getUserName())
							.appId(authentication.getAppId()).userId(authentication.getUserId()).build(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping("/verify")
	public ResponseEntity<String> verifyUser(@Param("code") String code) {
		if (commonService.getAuthenticationService().verify(code)) {
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail", HttpStatus.OK);
		}
	}

	@PostMapping(path = "forgotPassword/{appid}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@PathVariable("appid") String appid,
			@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess, @RequestBody String email) {
		try {
			Application application = commonService.getApplicationService().getByAppIdAndAccess(appid, appAccess);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appid);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(appid), HttpStatus.UNAUTHORIZED);
			}

			Optional<Authentication> auth = commonService.getAuthenticationService().getUserByEmail(email);
			if (!auth.isPresent()) {
				logger.error("User is not registered.:" + email);
				return new ResponseEntity<Exception>(new NoSuchUserFound(email), HttpStatus.BAD_REQUEST);
			}

			Authentication authentication = auth.get();
			authentication.setPassward(PasswordUtils.generateRandomPassword(10));
			
			Authentication tempAuthentication = new Authentication.AuthenticationBuilder()
					.userName(authentication.getUserName())
					.passward(authentication.getPassward())
					.build();
			
			authentication.setPassward(
					PasswordUtils.generateSecurePassword(authentication.getPassward(), application.getSalt()));

			commonService.getAuthenticationService().save(authentication);

			emailService.sendForgotPassword(tempAuthentication);

			return new ResponseEntity<>(tempAuthentication,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(path = "register/{appid}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@PathVariable("appid") String appid,
			@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
			@RequestHeader("X-HEADER-APP-URL") String appBaseUrl,
			@RequestBody Authentication authentication,
			HttpServletRequest request) {
		try {
			Application application = commonService.getApplicationService().getByAppIdAndAccess(appid, appAccess);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appid);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(appid), HttpStatus.UNAUTHORIZED);
			}

			Optional<Authentication> auth = commonService.getAuthenticationService()
					.getUserByEmail(authentication.getUserName());
			if (auth.isPresent()) {
				logger.error("User is already registeded.:" + authentication.getUserName());
				return new ResponseEntity<Exception>(new UserAlreadyExixts(authentication.getUserName()),
						HttpStatus.FOUND);
			}

			String randomCode = RandomString.make(64);
			authentication.setVerificationCode(randomCode);

			authentication.setPassward(
					PasswordUtils.generateSecurePassword(authentication.getPassward(), application.getSalt()));
			authentication.setAppId(application.getId());
			authentication = commonService.getAuthenticationService().save(authentication);
			if(StringUtils.isBlank(appBaseUrl))
				appBaseUrl = getSiteURL(request);
			
			emailService.sendVerificationEmail(authentication, appBaseUrl);
			return new ResponseEntity<>(
					new Authentication.AuthenticationBuilder().userName(authentication.getUserName())
							.appId(authentication.getAppId()).userId(authentication.getUserId()).build(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@DeleteMapping()
	public ResponseEntity<?> delete(@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
			@RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access) {
		try {
			Application application = commonService.getApplicationService().getByAppAccess(appAccess);
			if (application == null) {
				logger.error("Application is not registered with us");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}
			Boolean isDeleted = commonService.getAuthenticationService().deleteByToken(access);
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

}
