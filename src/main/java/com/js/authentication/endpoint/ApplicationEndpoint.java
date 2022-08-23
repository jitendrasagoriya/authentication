package com.js.authentication.endpoint;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.authentication.exception.ApplicationAlreadyRegistered;
import com.js.authentication.exception.NoSuchUserFound;
import com.js.authentication.model.Application;
import com.js.authentication.model.ApplicationAdmin;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.CommonService;
import com.js.authentication.token.SecureTokenGenerator;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/application/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ApplicationEndpoint {

	private final Logger logger = LoggerFactory.getLogger(ApplicationEndpoint.class);

	@Autowired
	private CommonService commonService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addApplication(@RequestHeader("X-AUTH-LOG-HEADER-APP-TOKEN") String token,
			@RequestBody Application application, HttpServletRequest request) {
		try {

			
			if (application == null) {
				return new ResponseEntity<>("application should not be null.", HttpStatus.BAD_REQUEST);
			}

			if (StringUtils.isBlank(application.getAppName()))
				return new ResponseEntity<>("App Name Should Not Null.", HttpStatus.BAD_REQUEST);

			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);

			if (!authentication.isPresent()) {
				return new ResponseEntity<>(new NoSuchUserFound(), HttpStatus.BAD_REQUEST);
			}

			Optional<Application> appOptional = commonService.getApplicationService()
					.getByName(application.getAppName());
			if (appOptional.isPresent()) {
				return new ResponseEntity<>(new ApplicationAlreadyRegistered(application.getAppName()),
						HttpStatus.BAD_REQUEST);
			}
			 
			String salt = PasswordUtils.getSalt(10);
			application.setId(SecureTokenGenerator.nextAppId(application.getAppName()));
			application.setAccess("ADMIN");
			application.setActive(Boolean.FALSE);
			application.setOnBoardTime(new Timestamp(System.currentTimeMillis()));
			application.setDescription(application.getDescription());
			application.setSalt(salt);
			application.setAccess(SecureTokenGenerator.getToken());
			
			Application result = commonService.getApplicationService().addNew(application);
			try {
				if (result != null && !StringUtils.isBlank(result.getId())) {
					ApplicationAdmin admin = ApplicationAdmin.builder().appId(result.getId())
							.userId(authentication.get().getUserId()).build();
					commonService.getApplicationAdminService().getRepository().saveAndFlush(admin);
				}
			} catch (Exception e) {
				commonService.getApplicationService().delete(result.getId());
			}

			return new ResponseEntity<Application>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllApplication(@RequestHeader("X-AUTH-LOG-HEADER-APP-TOKEN") String token) {
		try {

			Optional<Authentication> authentication = commonService.getAuthenticationService().getUserByToken(token);

			if (!authentication.isPresent()) {
				return new ResponseEntity<>("Invalid User.", HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<List<Application>>(commonService.getApplicationService().getApplicationByUserId(authentication.get().getUserId()), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = "{appid}")
	public ResponseEntity<?> getApplication(@PathVariable("appid") String appid,
			@RequestHeader("X-AUTH-LOG-HEADER") String access) {
		try {
			return new ResponseEntity<Application>(
					commonService.getApplicationService().getByAppIdAndAccess(appid, access), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(path = "page/{appid}")
	public ResponseEntity<?> getAuthenticationsForApplication(@PathVariable("appid") String appid,
			@RequestHeader("X-AUTH-LOG-HEADER") String access, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Authentication>>(
					commonService.getRegistrationByApplication(appid, access, pageable), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping(path = "{appid}")
	public ResponseEntity<?> deleteApplication(@PathVariable("appid") String appid,
			@RequestHeader("X-AUTH-LOG-HEADER") String access) {
		try {
			Boolean isDeleted = commonService.getApplicationService().deleteByAppIdAndAccess(appid, access);
			return new ResponseEntity<Boolean>(isDeleted, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}

	}

}
