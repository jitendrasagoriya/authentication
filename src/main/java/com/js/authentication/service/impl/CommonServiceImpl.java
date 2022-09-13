package com.js.authentication.service.impl;

 

import java.util.List;

import com.js.authentication.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;
import com.js.authentication.model.Authentication;

@Service
public class CommonServiceImpl implements CommonService {

    @SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private ApplicationAdminService applicationAdminService;
    
    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Integer totalRegistrationByApplication(String appId) throws NoSuchBeanException {
        Application application = null;
        application = applicationService.getById(appId);

        if(application != null) {
            List<Authentication> authentications = authenticationService.getByAppId(application.getId());
            return  authentications.size();
        }
        return 0;
    }

    @Override
    public Page<Authentication> getRegistrationByApplication(String appId, Pageable pageable) throws NoSuchBeanException {
        Application application = null;
        application = applicationService.getById(appId);

        if(application != null) {
            Page<Authentication> authentications = authenticationService.getByAppId(application.getId(),pageable);
            return  authentications;
        }
        return null;
    }

    @Override
    public Integer totalRegistrationByApplication(String appId, String access) throws NoSuchBeanException {
        Application application = null;
        application = applicationService.getByAppIdAndAccess(appId,access);

        if(application != null) {
            List<Authentication> authentications = authenticationService.getByAppId(application.getId());
            return  authentications.size();
        }
        return 0;
    }

    @Override
    public Page<Authentication> getRegistrationByApplication( String appId, String access, Pageable pageable ) throws NoSuchBeanException {
        Application application = null;
        application = applicationService.getByAppIdAndAccess(appId,access);

        if(application != null) {
            Page<Authentication> authentications = authenticationService.getByAppId(application.getId(),pageable);
            return authentications;
        }
        return null;
    }

    @Override
    public ApplicationService getApplicationService() {
        return applicationService;
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

	@Override
	public ApplicationAdminService getApplicationAdminService() {
		return applicationAdminService;
	}

	@Override
	public DashBoardService getDashBoardService() {
		return dashBoardService;
	}

    @Override
    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }


}
