package com.js.authentication.service.impl;

import com.js.authentication.dao.ApplicationDaoService;
import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;
import com.js.authentication.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private ApplicationDaoService applicationDaoService;

    @Override
    public Application addNew(Application application) {
        return applicationDaoService.addNew(application);
    }

    @Override
    public Application update(Application application) throws NoSuchBeanException {
        try {
            application = applicationDaoService.getById(application.getId()) ;
            if(application == null)
                throw new NoSuchBeanException("Your application is not registered with us");
        } catch (EntityNotFoundException e){
            throw new NoSuchBeanException("Your application is not registered with us");
        }
        return applicationDaoService.update(application);
    }

    @Override
    public Boolean delete(String id) throws NoSuchBeanException {
        return applicationDaoService.delete(id);
    }

    @Override
    public String getSaltByAccessKey(String accessKey) throws NoSuchBeanException {
        return applicationDaoService.getSaltByAccessKey(accessKey);
    }

    @Override
    public List<Application> getAll() {
        return applicationDaoService.getAll();
    }

    @Override
    public Page<Application> getAll(Pageable pageable) {
        return applicationDaoService.getAll(pageable);
    }

    @Override
    public Application getByName(String name) {
        return applicationDaoService.getByName(name);
    }

    @Override
    public Integer totalApplicationCount() {
        return getAll().size();
    }

    @Override
    public Integer totalActive() {
        return null;
    }

    @Override
    public Integer totalInActive() {
        return null;
    }

    @Override
    public Application getById(String id) throws NoSuchBeanException {
        return applicationDaoService.getById(id);
    }

    @Override
    public Application getByAppIdAndAccess(String id, String access) throws NoSuchBeanException {
        return applicationDaoService.getByAppIdAndAccess(id,access);
    }

    @Override
    public Application getByAppAccess(String access) throws NoSuchBeanException {
        return applicationDaoService.getByAppAccess(access);
    }

    @Override
    public Boolean deleteByAppIdAndAccess(String id, String access) throws NoSuchBeanException {
        return applicationDaoService.deleteByAppIdAndAccess(id,access);
    }
}
