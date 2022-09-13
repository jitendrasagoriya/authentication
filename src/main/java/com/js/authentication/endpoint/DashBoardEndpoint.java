package com.js.authentication.endpoint;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.authentication.dto.DashBoardDto;
import com.js.authentication.model.Authentication;
import com.js.authentication.model.DashBoard;
import com.js.authentication.service.CommonService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/admin/dashboard/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DashBoardEndpoint {

	private final Logger logger = LoggerFactory.getLogger(DashBoardEndpoint.class);

	@Autowired
	private CommonService service;
	
	

	@GetMapping
	public ResponseEntity<?> getDashBoardForAdmin(@RequestHeader("X-AUTH-LOG-HEADER-APP-TOKEN") String token) {
		try {
			
			Optional<Authentication> authentication = service.getAuthenticationService().getUserByToken(token);

			if (!authentication.isPresent()) {
				return new ResponseEntity<>("Invalid User.", HttpStatus.UNAUTHORIZED);
			}
			
			if(!authentication.get().isAppAdmin())
				return new ResponseEntity<>("User is not authorized.", HttpStatus.UNAUTHORIZED);
			
			DashBoardDto boardDto = new DashBoardDto();
			Optional<DashBoard> dashbaord = service.getDashBoardService().getDashboard(authentication.get().getUserId());
			if(!dashbaord.isPresent())
				return new ResponseEntity<DashBoardDto>(boardDto, HttpStatus.OK);
			
			boardDto = DashBoardDto.builder()
				.applicationCount(dashbaord.get().totalApplication())
				.userCount(dashbaord.get().totalAuthentication())
				.newUser(dashbaord.get().totalNewUsers())
				.data(dashbaord.get().getData())
				.newUsers(dashbaord.get().newUsers())
				.loggedInCount(dashbaord.get().totalLoggedInUsers())
			.build();
			
			return new ResponseEntity<DashBoardDto>(boardDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@GetMapping(path = "super/")
	public ResponseEntity<?> getDashBoardForSuperAdmin(@RequestHeader("X-AUTH-LOG-HEADER-APP-TOKEN") String token) {
		try {
			
			Optional<Authentication> authentication = service.getAuthenticationService().getUserByToken(token);

			if (!authentication.isPresent()) {
				return new ResponseEntity<>("Invalid User.", HttpStatus.UNAUTHORIZED);
			}
			
			if(authentication.get().isSuperAdmin())
				return new ResponseEntity<>("User is not authorized.", HttpStatus.UNAUTHORIZED);
			
			DashBoardDto boardDto = new DashBoardDto();
			Optional<DashBoard> dashbaord = service.getDashBoardService().getDashboard(authentication.get().getUserId());
			if(!dashbaord.isPresent())
				return new ResponseEntity<DashBoardDto>(boardDto, HttpStatus.OK);
			
			boardDto =DashBoardDto.builder()
				.applicationCount(dashbaord.get().totalApplication())
				.userCount(dashbaord.get().totalAuthentication())
				.newUser(dashbaord.get().totalNewUsers())
				.data(dashbaord.get().getData())
			.build();
			
			return new ResponseEntity<DashBoardDto>(boardDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		}
	}

}
