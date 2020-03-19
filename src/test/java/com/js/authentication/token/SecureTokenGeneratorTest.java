package com.js.authentication.token;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SecureTokenGeneratorTest {

    @Test
    public void getToken() {
        String token = SecureTokenGenerator.nextAppId("ANDOLAN");
        String token2 = SecureTokenGenerator.nextAppId("ANDOLAN");
        assertThat(token2).isNotEqualTo(token);
    }


}
