package com.js.authentication.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.js.authentication.builder.AuthenticationBuilder;
import com.js.authentication.model.Authentication;
import com.js.authentication.password.PasswordUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = {"classpath:authentication-data.sql"})
public class AuthenticationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthenticationRepository repository;

    @Test
    public void testIsActive() {
        Authentication authentication = AuthenticationBuilder.getAuthentication();
        entityManager.persist(authentication);
        entityManager.flush();

        Authentication found = repository.getActive("412563");
        assertThat(found.getUserName()).isEqualTo(authentication.getUserName());
    }

    @Test
    public void testGetApplication() {

        String salt = PasswordUtils.getSalt(30);

        Authentication authentication = AuthenticationBuilder.getAuthentication();
        authentication.setPassward(PasswordUtils.generateSecurePassword("123456", salt));
        entityManager.persist(authentication);
        entityManager.flush();

        Authentication found =
                repository.getAuthentication("D579446",PasswordUtils.generateSecurePassword("123456", salt));

        assertThat(found.getAppId()).isEqualTo("APPDOC");

    }

    @Test
    public void testGetAuthenticationByAppId () {
        List<Authentication> authentications = repository.getAuthenticationByAppId("MYAPP1");
        authentications.stream().forEach(System.out::println);
    }

    @Test
    public void testGetAuthenticationByAppIdPageable () {
        Pageable paging = PageRequest.of(0,1) ;
        Page<Authentication> authentications = repository.getAuthenticationByAppId("MYAPP1",paging);
        assertThat(authentications.getTotalElements()).isEqualTo(5);
    }

    @Test
    public void updateLoginTimestamp() {
        Authentication authentication = repository.getOne("USER8");
        int recordUpdated = repository.updateLoginTimestamp("HNDHDN655HD5DH");
        assertThat(recordUpdated).isEqualTo(1);
    }

    @Test
    public  void testLogOut() {
       int i = repository.logout("HNDHDN655HD5DH");
        assertThat(i).isEqualTo(1);
    }

    @Test
    public void testDeleteByToken() {
        int i = repository.deleteByToken("HNDHDN655HD5DH");
        assertThat(i)
                .isEqualTo(1);
    }

}
