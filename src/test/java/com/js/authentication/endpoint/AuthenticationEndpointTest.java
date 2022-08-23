package com.js.authentication.endpoint;


import com.js.authentication.AuthenticationApplication;
import com.js.authentication.dto.LoginDto;
import com.js.authentication.enums.UserType;
import com.js.authentication.model.Authentication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationEndpointTest {


    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    @Sql(statements = {
            "INSERT INTO APPLICATION (ID,APPNAME,ACCESS,SALT) " +
                    "VALUES ('MYID126','DOCTORAPP15','GHGI5KFJGFKD5','QyTi67RIpt');",

            "INSERT INTO AUTHENTICATION (USERNAME,PASSWORD,USERID,APPID) VALUES ('USER12','xzFn4adqm072hB7377Xa+YWJX/mtcymENN0tQS8a090=','USERID12','MYID126');"

    })
    public void getAccessToken() {
        headers.add("X-AUTH-LOG-HEADER-APP-ACCESS","GHGI5KFJGFKD5");
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDto loginDto = new LoginDto("USER12","myPassword123",UserType.APPUSER);
        HttpEntity<LoginDto> entity = new HttpEntity<LoginDto>(loginDto, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                createURLWithPort("/api/authentication/token/MYID126"),
                entity,
                String.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo( HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        System.out.println( responseEntity.getBody() );

    }

    @Test
    @Sql(statements = {
            "INSERT INTO APPLICATION (ID,APPNAME,ACCESS,SALT) " +
                    "VALUES ('MYID126','DOCTORAPP15','GHGI5KFJGFKD5','QyTi67RIpt');",

            "INSERT INTO AUTHENTICATION (USERNAME,PASSWORD,USERID,APPID,TOKEN) VALUES ('USER12','xzFn4adqm072hB7377Xa+YWJX/mtcymENN0tQS8a090=','USERID12','MYID126','QyTi67RIpt');"

    })
    public void getAuthentication() {
        headers.add("X-AUTH-LOG-HEADER-APP-ACCESS","GHGI5KFJGFKD5");
        headers.add("X-AUTH-LOG-HEADER-ACCESS","QyTi67RIpt");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Authentication> responseEntity = restTemplate.postForEntity(
                createURLWithPort("/api/authentication/auth/"),
                entity,
                Authentication.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo( HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        System.out.println( responseEntity.getBody() );

    }

    @Test
    @Sql(statements = {
            "INSERT INTO APPLICATION (ID,APPNAME,ACCESS,SALT) " +
                    "VALUES ('MYID127','DOCTORAPP17','GHGI5KFJGFKD5','QyTi67RIpt');",

            "INSERT INTO AUTHENTICATION (USERNAME,PASSWORD,USERID,APPID,TOKEN) VALUES ('USER13','xzFn4adqm072hB7377Xa+YWJX/mtcymENN0tQS8a090=','USERID13','MYID127','QyTi67RIpt');"

    })
    public void getAuthenticationApplicationNotFound() {
        headers.add("X-AUTH-LOG-HEADER-APP-ACCESS","GHGI5KFJGFKDKJ5");
        headers.add("X-AUTH-LOG-HEADER-ACCESS","QyTi67RIpt");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Authentication> responseEntity = restTemplate.postForEntity(
                createURLWithPort("/api/authentication/auth/"),
                entity,
                Authentication.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void addAuthentication() {
        headers.add("X-AUTH-LOG-HEADER-APP-ACCESS","GHGI5KFJGFKDKJ5");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Authentication authentication = new Authentication.AuthenticationBuilder("D579446",
                "Pass123")
                .build();
        HttpEntity<Authentication> entity = new HttpEntity<>(authentication, headers);

        ResponseEntity<Authentication> responseEntity = restTemplate.postForEntity(
                createURLWithPort("/api/authentication/"),
                entity,
                Authentication.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo( HttpStatus.OK);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
