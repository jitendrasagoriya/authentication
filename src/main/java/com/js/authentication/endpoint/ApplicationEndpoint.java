package com.js.authentication.endpoint;


import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;
import com.js.authentication.service.ApplicationService;
import com.js.authentication.service.CommonService;
import com.js.authentication.token.SecureTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/application/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ApplicationEndpoint {

    private final Logger logger = LoggerFactory.getLogger(ApplicationEndpoint.class);

    @Autowired
    private CommonService commonService;


    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> addApplication(@RequestBody Application application) {
        try {
            if (application != null) {
                application.setId(SecureTokenGenerator.nextAppId(application.getAppName()));
                application.setAccess("ADMIN");
                application.setActive(Boolean.FALSE);
                application.setOnBoardTime(new Timestamp(System.currentTimeMillis()));
                application.setDescription(application.getDescription());
                application.setSalt(  PasswordUtils.getSalt(10));
                application.setAccess(SecureTokenGenerator.getToken());
            }
            return new ResponseEntity<Application>(commonService.getApplicationService().addNew(application), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllApplication() {
        try {
            return new ResponseEntity<List>( commonService.getApplicationService().getAll(),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @GetMapping(path = "{appid}")
    public ResponseEntity<?> getApplication(@PathVariable("appid") String appid, @RequestHeader("X-AUTH-LOG-HEADER") String access) {
        try {
            return new ResponseEntity<Application>( commonService.getApplicationService().getByAppIdAndAccess(appid,access),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(path = "page/{appid}")
    public ResponseEntity<?> getAuthenticationsForApplication(@PathVariable("appid") String appid
            , @RequestHeader("X-AUTH-LOG-HEADER") String access, Pageable pageable) {
        try {
            return new ResponseEntity<Page>( commonService.getRegistrationByApplication(appid,access,pageable),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping(path = "{appid}")
    public ResponseEntity<?> deleteApplication(@PathVariable("appid") String appid
            , @RequestHeader("X-AUTH-LOG-HEADER") String access) {
        try {
              Boolean isDeleted =  commonService.getApplicationService().deleteByAppIdAndAccess(appid,access);
              return new ResponseEntity<Boolean>(isDeleted,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }

    }
}
