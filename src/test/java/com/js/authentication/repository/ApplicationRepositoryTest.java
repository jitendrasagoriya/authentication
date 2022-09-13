package com.js.authentication.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.js.authentication.builder.ApplicationBuilder;
import com.js.authentication.model.Application;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = {"classpath:application-data.sql"})
public class ApplicationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;


    @Autowired
    private ApplicationRepository repository;

    @Test
    public void testGetApplicationByAccessToken() {
        Application application = ApplicationBuilder.getApplication();
        entityManager.persist(application);
        entityManager.flush();

        Application found = repository.getApplicationByAccessToken(application.getAccess());

        assertThat(found.getId()).isEqualTo(application.getId());
    }

    @Test
    public void testGetApplicationByName() {
        Application found = repository.getApplicationByName("DOCTORAPP4");
        assertThat(found.getId()).isEqualTo("MYID126");
    }

    @Test
    public void testGetApplicationByIdAndAccessNotFound() {
        Application found = repository.getApplicationByIDAndAccessToken("MYID125","BVBCVBB");
        assertThat(found).isNull();
    }

    @Test
    public void testGetApplicationByIdAndAccess() {
        Application found = repository.getApplicationByIDAndAccessToken("KFJF5KFJGFKD5","MYID125");
        assertThat(found.getAppName()).isEqualTo("DOCTORAPP3");
    }


    @Test
    public void testGetApplicationByNameNotFound() {
        Application found = repository.getApplicationByName("jdhfdjhf");
        assertThat(found).isNull();
    }

    @Test
    public void testDeleteApplicationByAppidAndAccess() {
        int deleteCount = repository.deleteApplicationByAppidAndAccess("GHGSDFSSFFKD5","MYID126");
        assertThat(deleteCount)
                .isEqualTo(1);
    }

}
