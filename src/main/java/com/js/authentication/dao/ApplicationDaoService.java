package com.js.authentication.dao;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;

@Service
public interface ApplicationDaoService<R> extends BaseSerivce<R> {

    public Application addNew(Application application);

    public Application getById(String id) throws EntityNotFoundException;

    public List<Application> getAll();

    public Page<Application> getAll(Pageable pageable);

    public Optional<Application> getByName(String name);

    public Application update(Application application);

    public Boolean delete(String id) throws NoSuchBeanException;

    public String getSaltByAccessKey(String accessKey) throws NoSuchBeanException;

    public Application getByAppIdAndAccess(String id, String access) throws NoSuchBeanException;

    public Application getByAppAccess( String access) throws NoSuchBeanException;

    public Boolean deleteByAppIdAndAccess(String id,String access) throws NoSuchBeanException;
    
    List<Application> getApplicationByUserId( String id);
}
