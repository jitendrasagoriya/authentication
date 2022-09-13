package com.js.authentication.endpoint;

import com.js.authentication.dto.ApplicationUser;
import com.js.authentication.dto.ChangePassword;
import com.js.authentication.dto.LoginDto;
import com.js.authentication.enums.UserType;
import com.js.authentication.exception.*;
import com.js.authentication.mail.service.impl.EmailServiceImpl;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import com.js.authentication.model.UserDetails;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.CommonService;

import net.bytebuddy.utility.RandomString;

import java.util.List;
import java.util.Optional;

import javax.management.InvalidApplicationException;
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

	@PostMapping(path = "logout/")
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

	@GetMapping(path = "auth/")
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
			responce.setVerificationCode("");

			return new ResponseEntity<>(responce, HttpStatus.OK);
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
					.generateAccessToken(loginDto.getUserName(), loginDto.getPassword(), application.getSalt(),UserType.APPUSER,appid);
 
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
			authentication.setToken(null);
			authentication.setIsLogout(Boolean.TRUE);
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
	public ResponseEntity<?> forgotPassword(@PathVariable("appid") String appid,
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

			authentication.setToken(null);
			authentication.setIsLogout(Boolean.TRUE);
			
			commonService.getAuthenticationService().save(authentication);

			emailService.sendForgotPassword(tempAuthentication);

			return new ResponseEntity<>(tempAuthentication,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping(path = "", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestHeader("X-AUTH-LOG-HEADER-TOKEN") String token,
								 @RequestBody Authentication authentication,
								 HttpServletRequest request) {
		try{
			Optional<Authentication> auth = commonService.getAuthenticationService().getUserByToken(token);
			if(!auth.isPresent()) {
				return new ResponseEntity<Exception>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);
			}

			Optional<Application> application = commonService.getApplicationService()
					.getApplicationByUserIdAndApplication(auth.get().getUserId(),authentication.getAppId());

			if(!application.isPresent()) {
				return new ResponseEntity<String>("Invalid Request.", HttpStatus.BAD_REQUEST);
			}
			Optional<Authentication> requestAuthentication = commonService.getAuthenticationService()
					.getAuthenticationById(authentication.getUserId());

			if(!requestAuthentication.isPresent()) {
				return new ResponseEntity<Exception>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);
			}

			Authentication finalAuthentication = requestAuthentication.get();

			finalAuthentication.setUserName(authentication.getUserName());
			finalAuthentication.setUserType(authentication.getUserType());
			finalAuthentication.setIsActive(authentication.getIsActive());
			finalAuthentication.setIsLogout(authentication.getIsLogout());
			return new ResponseEntity<>(commonService.getAuthenticationService().updateAuthentication(finalAuthentication), HttpStatus.OK);

		}catch (Exception e){
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}

	}

	@DeleteMapping(path = "{id}/")
	public ResponseEntity<?> deleteById(@RequestHeader("X-AUTH-LOG-HEADER-TOKEN") String token,
									@PathVariable("id") String id ) {
		logger.info("deleteById {}",id);
		if(logger.isDebugEnabled())
			logger.debug("Token => {}",token);
		try{
			Optional<Authentication> auth = commonService.getAuthenticationService().getUserByToken(token);
			if(logger.isDebugEnabled())
				logger.debug("Auth is present => {}",auth.isPresent());
			if(!auth.isPresent()) {
				return new ResponseEntity<Exception>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);
			}

			if(logger.isDebugEnabled())
				logger.debug("User is Admin => {}",!auth.get().getUserType().getValue().equals(UserType.APPADMIN.getValue()) ||
						!auth.get().getUserType().getValue().equals(UserType.APPADMIN.getValue()));

			if(!auth.get().getUserType().getValue().equals(UserType.APPADMIN.getValue()) ||
					!auth.get().getUserType().getValue().equals(UserType.APPADMIN.getValue())) {
				return new ResponseEntity<Exception>(new UserNotAuthorized(), HttpStatus.UNAUTHORIZED);
			}

			Optional<Authentication> requestedUser = commonService.getAuthenticationService().getAuthenticationById(id);

			if(!requestedUser.isPresent()) {
				return new ResponseEntity<Exception>(new NoSuchBeanException("User Not Found!!"), HttpStatus.UNAUTHORIZED);
			}

			List<Application> application = commonService.getApplicationService().getApplicationByUserId(auth.get().getUserId());
			Optional<Application> searchApplication =  application.stream().filter(a -> StringUtils.equalsIgnoreCase(a.getId(),requestedUser.get().getAppId())).findAny();

			if(logger.isDebugEnabled())
				logger.debug("Application is present => {}",auth.isPresent());

			if(!searchApplication.isPresent()) {
				return new ResponseEntity<Exception>(new Exception("Invalid User."), HttpStatus.UNAUTHORIZED);
			}

			Boolean deleted = commonService.getAuthenticationService().deleteById(requestedUser.get().getUserId());

			if(logger.isDebugEnabled())
				logger.debug("User Deleted => {}",deleted);

			return new ResponseEntity<>(deleted, HttpStatus.OK);

		}catch (Exception e){
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
			if(authentication.getUserType() == null)
				authentication.setUserType(UserType.APPUSER);
			
			Optional<Authentication> auth = commonService.getAuthenticationService()
					.getUserByEmail(authentication.getUserName(),authentication.getUserType(),appid);
			
			if (auth.isPresent()) {
				if (auth.get().getAppId().equalsIgnoreCase(appid)) {
					logger.error("User is already registeded.:" + authentication.getUserName());
					return new ResponseEntity<Exception>(new UserAlreadyExixts(authentication.getUserName()),
							HttpStatus.FOUND);
				}
			}

			String randomCode = RandomString.make(64);
			authentication.setVerificationCode(randomCode);
			
			authentication.setPassward(
					PasswordUtils.generateSecurePassword(authentication.getPassward(), application.getSalt()));
			authentication.setAppId(application.getId());
			authentication = commonService.getAuthenticationService().saveAuthentication(authentication);
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

	@GetMapping(path = "/byUser")
	public ResponseEntity<?> getUserByAdminId(@RequestHeader("X-AUTH-LOG-HEADER-TOKEN") String token) {
		try {
			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent()) {
				logger.error("Unauthorized User.");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<>(commonService.getApplicationAdminService().
					getListOfAuthenticationWithApplicationNameByUserId(authentication.get().getUserId()), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = "/byUser/{id}")
	public ResponseEntity<?> getUserByAdminId(@RequestHeader("X-AUTH-LOG-HEADER-TOKEN") String token , @PathVariable("id") String id) {
		try {
			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent()) {
				logger.error("Unauthorized User.");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}

			Optional<ApplicationUser> applicationUser = commonService.getApplicationAdminService().
					getAuthenticationWithApplicationNameByUserId(authentication.get().getUserId(),id);
			if(!applicationUser.isPresent())
				return new ResponseEntity<>("User Not Found.", HttpStatus.OK);

			return new ResponseEntity<>(applicationUser.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = "/details/")
	public ResponseEntity<?> getUserDetails(@RequestHeader("X-AUTH-LOG-HEADER-TOKEN") String token) {
		try {
			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent()) {
				logger.error("Unauthorized User.");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}

			UserDetails usrDetails = UserDetails.builder()
					.id(authentication.get().getUserId())
					.build();

			Optional<UserDetails> userDetails = commonService.getUserDetailsService().getUserDetails(authentication.get().getUserId());

			if(!userDetails.isPresent()) {
				logger.error("Unauthorized User.");
				return new ResponseEntity<Exception>(new UserDetailsNotFound(authentication.get().getUserId()), HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<>(userDetails.get(), HttpStatus.OK);

		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(path = "/details/")
	public ResponseEntity<?> save(@RequestHeader("X-AUTH-LOG-HEADER-TOKEN") String token , @RequestBody UserDetails userDetails) {
		try {
			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);
			if (!authentication.isPresent()) {
				logger.error("Unauthorized User.");
				return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
			}
			userDetails.setId(authentication.get().getUserId());
			Optional<UserDetails> details = commonService.getUserDetailsService().save(userDetails);
			if(!details.isPresent()) {
				return new ResponseEntity<Exception>(new Exception("Invalid User Details!!"), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(details.get(), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}


}
