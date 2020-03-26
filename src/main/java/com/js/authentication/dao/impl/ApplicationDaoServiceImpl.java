package com.js.authentication.dao.impl;

import com.js.authentication.dao.ApplicationDaoService;
import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;
import com.js.authentication.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ApplicationDaoServiceImpl implements ApplicationDaoService<ApplicationRepository> {

    private final Logger logger = LoggerFactory.getLogger(ApplicationDaoServiceImpl.class);

    @Autowired
    private ApplicationRepository repository;

    @Override
    public Application addNew(Application application) {
        return repository.saveAndFlush(application);
    }

    @Override
    public Application getById(String id) throws EntityNotFoundException {
        return repository.getOne(id);
    }

    @Override
    public List<Application> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<Application> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Application getByName(String name) {
        return repository.getApplicationByName(name);
    }

    @Override
    public Application update(Application application) {
        return repository.saveAndFlush(application);
    }

    @Override
    public Boolean delete(String id) throws NoSuchBeanException {
        Application application = null;
        try {
            application = repository.getOne(id);
            if(application == null)
                throw new NoSuchBeanException("Application is not registered with us.");
            repository.delete(application);
            return true;
        } catch (EntityNotFoundException e) {
            throw new NoSuchBeanException("Application is not registered with us.");
        } catch (Exception e) {
            logger.error("Exception during delete application : AppId"+ application.getId());
            return false;
        }
    }

    @Override
    public String getSaltByAccessKey(String accessKey) throws NoSuchBeanException {
        Application application = repository.getApplicationByAccessToken(accessKey);
        if(application == null)
            throw new NoSuchBeanException("Application is not registered with us.");
        return application.getSalt();
    }

    @Override
    public Application getByAppIdAndAccess(String id, String access) throws NoSuchBeanException {
        return repository.getApplicationByIDAndAccessToken(access,id);
    }

    @Override
    public Application getByAppAccess(String access) throws NoSuchBeanException {
        return repository.getApplicationByAccessToken(access);
    }

    @Override
    @Transactional
    public Boolean deleteByAppIdAndAccess(String id, String access) throws NoSuchBeanException {
        return repository.deleteApplicationByAppidAndAccess(access,id)>0?Boolean.TRUE:Boolean.FALSE;
    }

    @Override
    public ApplicationRepository getRepository() {
        return repository;
    }
}
