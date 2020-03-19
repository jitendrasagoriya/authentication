package com.js.authentication.dao;


import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public interface ApplicationDaoService<R> extends BaseSerivce<R> {

    public Application addNew(Application application);

    public Application getById(String id) throws EntityNotFoundException;

    public List<Application> getAll();

    public Page<Application> getAll(Pageable pageable);

    public Application getByName(String name);

    public Application update(Application application);

    public Boolean delete(String id) throws NoSuchBeanException;

    public String getSaltByAccessKey(String accessKey) throws NoSuchBeanException;

    public Application getByAppIdAndAccess(String id, String access) throws NoSuchBeanException;

    public Application getByAppAccess( String access) throws NoSuchBeanException;
}
