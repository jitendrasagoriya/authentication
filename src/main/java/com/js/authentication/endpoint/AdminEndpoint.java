package com.js.authentication.endpoint;


import com.js.authentication.model.Application;
import com.js.authentication.password.PasswordUtils;
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

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/admin/js/application/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AdminEndpoint {

    private final Logger logger = LoggerFactory.getLogger(ApplicationEndpoint.class);

    @Autowired
    private CommonService commonService;

    @GetMapping(path = {""}  )
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return new ResponseEntity<Page>(commonService.getAuthenticationService().getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @GetMapping(path = {"{appid/}","{appid}"} )
    public ResponseEntity<?> getAll(@PathVariable("appid") String applicationId , Pageable pageable) {
        try {
            return new ResponseEntity<Page>(commonService.getAuthenticationService()
                    .getByAppId(applicationId,pageable), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
