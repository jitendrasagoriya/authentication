package com.js.authentication.dao;

import com.js.authentication.builder.AuthenticationBuilder;
import com.js.authentication.config.JpaTestConfig;
import com.js.authentication.config.TestConfig;
import com.js.authentication.dao.impl.AuthenticationDaoServiceImpl;
import com.js.authentication.model.Authentication;
import com.js.authentication.token.SecureTokenGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { JpaTestConfig.class, TestConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class AuthenticationDaoServiceImplTest {

    @Autowired
    private AuthenticationDaoServiceImpl authenticationDaoService;

    @Test
    public void testByToken() {
        String token = SecureTokenGenerator.nextAppId("DOCAPP");
        Authentication authentication = AuthenticationBuilder.getAuthentication();
        authentication.setToken(token);

        authenticationDaoService.getRepository().save(authentication);
        Authentication found = authenticationDaoService.getUserByToken(token);
        assertThat(found.getAppId())
                .isEqualTo(authentication.getAppId());
    }

    @Test
    public void testByUsernamePassword() {
        String token = SecureTokenGenerator.nextAppId("DOCAPP");
        Authentication authentication = AuthenticationBuilder.getAuthentication();
        authentication.setToken(token);

        authenticationDaoService.getRepository().save(authentication);
        Authentication found = authenticationDaoService.getApplication("D579446","123456");
        assertThat(found.getToken())
                .isEqualTo(token);
    }



}
