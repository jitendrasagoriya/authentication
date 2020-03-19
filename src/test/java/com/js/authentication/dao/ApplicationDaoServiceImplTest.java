package com.js.authentication.dao;


import com.js.authentication.builder.ApplicationBuilder;
import com.js.authentication.config.JpaTestConfig;
import com.js.authentication.config.TestConfig;
import com.js.authentication.exception.NoSuchBeanException;
import com.js.authentication.model.Application;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { JpaTestConfig.class, TestConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
@Sql(scripts = {"classpath:application-data.sql"})
public class ApplicationDaoServiceImplTest {

    @Autowired
    private ApplicationDaoService applicationDaoService;

    @Test
    public void testSave() {
        Application application = ApplicationBuilder.getApplication();
        applicationDaoService.addNew(application);
    }

    @Test
    public void testGetAll(){
        List<Application> applications = applicationDaoService.getAll();
        assertThat(applications.size()).isEqualTo(4);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveDuplicate() {
        Application application = ApplicationBuilder.getApplication();
        applicationDaoService.addNew(application);

        Application application2 = ApplicationBuilder.getApplication();
        applicationDaoService.addNew(application2);
    }

    @Test
    public void update() {
        Application application = ApplicationBuilder.getApplication();
        application = applicationDaoService.addNew(application);
        String access = application.getSalt();
        application.setSalt("SALT2");
        Application update =  applicationDaoService.update(application);

        assertThat(update.getSalt()).isNotEqualTo(access);
    }

    @Test
    public void testDeleteNoSuchBeanException() throws NoSuchBeanException {
        applicationDaoService.delete("12");
    }

    @Test
    public void testDelete() throws NoSuchBeanException {
        Application application = ApplicationBuilder.getApplication();
        applicationDaoService.addNew(application);

        Boolean result = applicationDaoService.delete(application.getId());

        assertThat(result).isTrue();
    }

    @Test
    public void testGetById() {
        Application application = applicationDaoService.getById("MYID123");
        assertThat(application.getAccess()).isEqualTo("GHGI5KFJGFKD5");
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetByIdNotFound() {
        Application application = applicationDaoService.getById("MYID122");
        assertThat(application).isNull();
    }
}
