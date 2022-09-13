package com.js.authentication.endpoint;

import java.sql.Timestamp;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.authentication.dto.LoginDto;
import com.js.authentication.dto.LoginResponce;
import com.js.authentication.enums.UserType;
import com.js.authentication.exception.ApplicationAlreadyRegistered;
import com.js.authentication.exception.ApplicationNotRegistered;
import com.js.authentication.exception.InvalidUserNameAndPassword;
import com.js.authentication.exception.NoSuchUserFound;
import com.js.authentication.exception.UserAlreadyExixts;
import com.js.authentication.exception.UserNotAuthorized;
import com.js.authentication.exception.UserNotRegisteredWithApplication;
import com.js.authentication.exception.UserNotVerified;
import com.js.authentication.mail.service.impl.EmailServiceImpl;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.CommonService;
import com.js.authentication.token.SecureTokenGenerator;

import net.bytebuddy.utility.RandomString;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/admin/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AdminEndpoint {

	private final Logger logger = LoggerFactory.getLogger(AdminEndpoint.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private EmailServiceImpl emailService;

	@Value("${common.auth.super.app.id}")
	private String appId;

	@Value("${common.auth.super.app.access}")
	private String appAccess;

	@GetMapping(path = { "user/list/", "user/list" })
	public ResponseEntity<?> getUsers(@RequestHeader(name = "X-AUTH-HEADER-TOKEN") String token, Pageable pageable) {
		try {

			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent())
				return new ResponseEntity<>(new NoSuchUserFound(), HttpStatus.UNAUTHORIZED);

			if (!StringUtils.equalsAnyIgnoreCase(authentication.get().getUserType().getValue(),
					UserType.SUPERADMIN.getValue()))
				return new ResponseEntity<>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(commonService.getAuthenticationService().getAll(pageable), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = { "user/list/{id}/", "user/list/{id}" })
	public ResponseEntity<?> getApplicationUser(@PathVariable("id") String id,
			@RequestHeader(name = "X-AUTH-HEADER-TOKEN") String token, Pageable pageable) {
		try {

			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent())
				return new ResponseEntity<>(new NoSuchUserFound(), HttpStatus.UNAUTHORIZED);

			if (!StringUtils.equalsAnyIgnoreCase(authentication.get().getUserType().getValue(),
					UserType.SUPERADMIN.getValue()))
				return new ResponseEntity<>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);

			Authentication search = new Authentication();
			search.setUserType(UserType.APPUSER);
			search.setAppId(id);
			return new ResponseEntity<>(commonService.getAuthenticationService().getAll(search, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = { "user/admin/list/", "user/admin/list" })
	public ResponseEntity<?> getApplicationOwner(@RequestHeader(name = "X-AUTH-HEADER-TOKEN") String token,
			Pageable pageable) {
		try {

			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent())
				return new ResponseEntity<>(new NoSuchUserFound(), HttpStatus.UNAUTHORIZED);

			if (!StringUtils.equalsAnyIgnoreCase(authentication.get().getUserType().getValue(),
					UserType.SUPERADMIN.getValue()))
				return new ResponseEntity<>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);

			Authentication search = new Authentication();
			search.setUserType(UserType.APPADMIN);

			return new ResponseEntity<>(commonService.getAuthenticationService().getAll(search, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = { "apps/", "apps" })
	public ResponseEntity<?> getAllByApp(@RequestHeader(name = "X-AUTH-HEADER-TOKEN") String token, Pageable pageable) {
		try {
			String applicationId = "";
			Authentication search = new Authentication();
			search.setUserType(UserType.APPUSER);
			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent())
				return new ResponseEntity<>(new NoSuchUserFound(), HttpStatus.UNAUTHORIZED);

			if (StringUtils.equalsAnyIgnoreCase(authentication.get().getUserType().getValue(),
					UserType.APPADMIN.getValue()))
				search.setAppId(applicationId);

			return new ResponseEntity<>(commonService.getAuthenticationService().getByAppId(applicationId, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = { "apps/{id}", "apps/{id}/" })
	public ResponseEntity<?> getAllByAppID(@RequestHeader(name = "X-AUTH-HEADER-TOKEN") String token,
			Pageable pageable) {
		try {
			String applicationId = "";
			return new ResponseEntity<>(commonService.getAuthenticationService().getByAppId(applicationId, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(path = "token/", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAccessToken(@RequestBody LoginDto loginDto) {
		if (logger.isDebugEnabled())
			logger.debug("Fetching access token..");
		try {
			Application application = commonService.getApplicationService().getByAppIdAndAccess(appId, appAccess);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appId);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(appId), HttpStatus.UNAUTHORIZED);
			}

			Optional<String> accessToken = commonService.getAuthenticationService().generateAccessToken(
					loginDto.getUserName(), loginDto.getPassword(), application.getSalt(), UserType.APPADMIN, appId);

			return new ResponseEntity<String>(accessToken.orElseThrow(() -> new InvalidUserNameAndPassword()),
					HttpStatus.OK);
		} catch (UserNotVerified | UserNotRegisteredWithApplication e) {
			return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchUserFound noSuchUserFound) {
			return new ResponseEntity<>(new InvalidUserNameAndPassword(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}

	}

	@PostMapping(path = "login/", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		if (logger.isDebugEnabled())
			logger.debug("Fetching access token..");
		try {
			Application application = commonService.getApplicationService().getByAppIdAndAccess(appId, appAccess);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appId);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(appId), HttpStatus.UNAUTHORIZED);
			}

			Optional<String> accessToken = commonService.getAuthenticationService().generateAccessToken(
					loginDto.getUserName(), loginDto.getPassword(), application.getSalt(), UserType.APPADMIN, appId);

			Optional<Authentication> auth = commonService.getAuthenticationService().getUserByToken(accessToken.get());

			if (!auth.isPresent())
				return new ResponseEntity<Exception>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);

			LoginResponce responce = LoginResponce.builder()
					.userId(auth.get().getUserId())
					.userName(auth.get().getUserName())
					.userType(auth.get().getUserType())
					.token(auth.get().getToken())
					.build();

			return new ResponseEntity<>(responce,
					HttpStatus.OK);
		} catch (UserNotVerified | UserNotRegisteredWithApplication e) {
			return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchUserFound noSuchUserFound) {
			return new ResponseEntity<>(new InvalidUserNameAndPassword(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}

	}

	@PostMapping(path = "addApplication", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addApplication(@RequestBody Application application, HttpServletRequest request) {
		try {

			if (StringUtils.isBlank(application.getAppName()))
				return new ResponseEntity<>("App Name Should Not Null.", HttpStatus.BAD_REQUEST);

			Optional<Application> appOptional = commonService.getApplicationService()
					.getByName(application.getAppName());
			if (appOptional.isPresent()) {
				return new ResponseEntity<>(new ApplicationAlreadyRegistered(application.getAppName()),
						HttpStatus.BAD_REQUEST);
			}

			String salt = PasswordUtils.getSalt(10);
			application.setId(SecureTokenGenerator.nextAppId(application.getAppName()));
			application.setAccess("SUPERADMIN");
			application.setActive(Boolean.TRUE);
			application.setOnBoardTime(new Timestamp(System.currentTimeMillis()));
			application.setDescription(application.getDescription());
			application.setSalt(salt);
			application.setAccess(SecureTokenGenerator.getToken());

			Application result = commonService.getApplicationService().addNew(application);
			return new ResponseEntity<Application>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(path = "register/", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestHeader("X-HEADER-APP-URL") String appBaseUrl,
			@RequestBody Authentication authentication, HttpServletRequest request) {
		try {

			Application application = commonService.getApplicationService().getByAppIdAndAccess(appId, appAccess);

			if (application == null) {
				logger.error("Application is not registered with us, Application id is :" + appId);
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(appId), HttpStatus.UNAUTHORIZED);
			}

			authentication.setUserType(UserType.APPADMIN);

			Optional<Authentication> auth = commonService.getAuthenticationService()
					.getUserByEmail(authentication.getUserName(), authentication.getUserType(), appId);
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
			authentication = commonService.getAuthenticationService().saveAuthentication(authentication);
			if (StringUtils.isBlank(appBaseUrl))
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

	@GetMapping(path = "auth/")
	public ResponseEntity<?> getAuthentication(@RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access) {
		try {
			Authentication authentication = null;
			authentication = commonService.getAuthenticationService().login(access);
			if (authentication != null) {
				if (!authentication.getIsActive()) {
					logger.error("User is not verified.");
					return new ResponseEntity<Exception>(new UserNotVerified("User is not verified."), HttpStatus.OK);
				}
			}
			Authentication responce = new Authentication.AuthenticationBuilder(authentication).build();
			authentication = new Authentication();
			authentication.setUserName(responce.getUserName());
			authentication.setUserId(responce.getUserId());
			authentication.setUserType(responce.getUserType());
			authentication.setToken(responce.getToken());

			return new ResponseEntity<>(authentication, HttpStatus.OK);
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

}
