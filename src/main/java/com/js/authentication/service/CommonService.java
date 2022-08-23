package com.js.authentication.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Authentication;

@Service
public interface CommonService {

        public Integer totalRegistrationByApplication(String appId) throws NoSuchBeanException;

        public Page<Authentication> getRegistrationByApplication(String appId, Pageable pageable) throws NoSuchBeanException;

        public Integer totalRegistrationByApplication(String appId,String access) throws NoSuchBeanException;

        public Page<Authentication> getRegistrationByApplication(String appId, String access, Pageable pageable) throws NoSuchBeanException;

        public ApplicationService getApplicationService();

        public AuthenticationService getAuthenticationService();
        
        public ApplicationAdminService getApplicationAdminService();
}
