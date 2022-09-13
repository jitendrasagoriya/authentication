package com.js.authentication.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.authentication.service.CommonService;

import springfox.documentation.annotations.ApiIgnore;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class VerifyEndpoint {
	
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(ApplicationEndpoint.class);

	@Autowired
	private CommonService commonService;
	
	@ApiIgnore
	@GetMapping( path = {"verify/","verify"})
	public ResponseEntity<String> verifyUser(@Param("code") String code) {
		if (commonService.getAuthenticationService().verify(code)) {
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail", HttpStatus.OK);
		}
	}

}
