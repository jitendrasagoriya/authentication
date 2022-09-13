package com.js.authentication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;

@Service
public interface ApplicationService {

    public Application addNew(Application application);
    public Application update(Application application) throws NoSuchBeanException;
    public Boolean delete(String id) throws NoSuchBeanException;
    public String getSaltByAccessKey(String accessKey) throws NoSuchBeanException;
    public List<Application> getAll();
    public Page<Application> getAll(Pageable pageable);
    public  Optional<Application> getByName(String name);
    public Integer totalApplicationCount();
    public Integer totalActive();
    public Integer totalInActive();
    public Application getById(String id) throws NoSuchBeanException;
    public Application getByAppIdAndAccess(String id, String access) throws NoSuchBeanException;
    public Application getByAppAccess(String access) throws NoSuchBeanException;
    public Boolean deleteByAppIdAndAccess(String id, String access) throws NoSuchBeanException;
    List<Application> getApplicationByUserId( String id);
    public Optional<Application> getApplicationByUserIdAndApplication(String id, String appid) ;
}
