package com.js.authentication.endpoint;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonObject;

import com.js.authentication.AuthenticationApplication;
import com.js.authentication.builder.ApplicationBuilder;

import com.js.authentication.model.Application;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.http.*;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationEndpointTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();



    @Test
    @Sql(statements = {"INSERT INTO APPLICATION (ID,APPNAME,ACCESS,SALT) VALUES ('MYID123','DOCTORAPP1','GHGI5KFJGFKD5','DJFDJFD');"})
    public void testGetAllApplication() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(createURLWithPort("/api/application/"), HttpMethod.GET,entity,String.class);

        Gson gson = new GsonBuilder().create();
        List<Application> applications =  gson.fromJson(response.getBody(),List.class );
        assertThat(applications.size()).isEqualTo(1);
    }

    @Test
    public void testAddApplication() {
        Application application = ApplicationBuilder.getApplication();
        HttpEntity<Application> entity = new HttpEntity<Application>(application, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/application/"),
                HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @Sql(statements = {"INSERT INTO APPLICATION (ID,APPNAME,ACCESS,SALT) VALUES ('MYID124','DOCTORAPP12','GHGI5KFJGFKD5','DJFDJFD');"})
    public void testGetByAppIdAndAccess() {
        headers.add("X-AUTH-LOG-HEADER","GHGI5KFJGFKD5");

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(createURLWithPort("/api/application/MYID124"), HttpMethod.GET,entity,String.class);

        Gson gson = new GsonBuilder().create();
        Application applications =  gson.fromJson(response.getBody(),Application.class );
        assertThat(applications).isNotNull();
        assertThat(applications.getAppName()).isEqualTo("DOCTORAPP12");
    }

    @Test
    @Sql(statements = {
            "INSERT INTO APPLICATION (ID,APPNAME,ACCESS,SALT) " +
                    "VALUES ('MYID125','DOCTORAPP13','GHGI5KFJGFKD5','DJFDJFD');",
            "INSERT INTO AUTHENTICATION (USERNAME,PASSWORD,USERID,APPID) VALUES ('USER1','PASSWORD','USERID1','MYID125');",
            "INSERT INTO AUTHENTICATION (USERNAME,PASSWORD,USERID,APPID) VALUES ('USER2','PASSWORD','USERID2','MYID125');"
    })
    public void testGetAuthenticationsForApplication() {
        headers.add("X-AUTH-LOG-HEADER","GHGI5KFJGFKD5");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(createURLWithPort("/api/application/page/MYID125"),
                        HttpMethod.GET,entity,String.class);

        Gson gson = new GsonBuilder().create();
        JsonObject found =  gson.fromJson(response.getBody(), JsonObject.class);
        JsonObject firstElement = found.getAsJsonArray("content").get(0).getAsJsonObject();
        assertThat(found.getAsJsonArray("content").size()).isEqualTo(2);
        assertThat(firstElement.get("userName").getAsString()).isEqualTo("USER1");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
