package com.js.authentication.endpoint;

import com.js.authentication.dto.LoginDto;
import com.js.authentication.exception.ApplicationNotRegistered;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import com.js.authentication.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
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

    @PostMapping(path = "logout/" ,consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> logout(@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
                                               @RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access) {
        Boolean isLogout = false;
        try {

            Application application = commonService.getApplicationService().getByAppAccess(appAccess);
            if(application == null) {
                logger.error("Application is not registered with us");
                return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
            }
            isLogout = commonService.getAuthenticationService().logOut(access);

           return new ResponseEntity<>(isLogout, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(path = "auth/" ,consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAuthentication(@RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String appAccess,
                                               @RequestHeader("X-AUTH-LOG-HEADER-ACCESS") String access) {
        try {

            Application application = commonService.getApplicationService().getByAppAccess(appAccess);
            if(application == null) {
                logger.error("Application is not registered with us");
                return new ResponseEntity<Exception>(new ApplicationNotRegistered(), HttpStatus.UNAUTHORIZED);
            }

            Authentication authentication = null;

            authentication = commonService.getAuthenticationService().login(access);

            return new ResponseEntity<>(authentication, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(path = "token/{appid}" ,consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAccessToken(@PathVariable("appid") String appid,
                                            @RequestHeader("X-AUTH-LOG-HEADER-APP-ACCESS") String access,
                                            @RequestBody LoginDto loginDto) {
        try {
            Application application = commonService.getApplicationService().getByAppIdAndAccess(appid,access);

            if(application == null) {
                logger.error("Application is not registered with us, Application id is :"+appid);
                return new ResponseEntity<Exception>(new ApplicationNotRegistered(appid), HttpStatus.UNAUTHORIZED);
            }

            String accessToken = commonService.getAuthenticationService()
                    .generateAccessToken(loginDto.getUserName(),loginDto.getPassword(),application.getSalt());

            return new ResponseEntity<String>( accessToken,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }

    }

    }
