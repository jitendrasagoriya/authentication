package com.js.authentication.builder;

import com.js.authentication.model.Authentication;

public class AuthenticationBuilder {

    public static Authentication getAuthentication() {
            return new Authentication.AuthenticationBuilder("D579446","123456")
                    .appId("APPDOC")
                    .userId("D579446")
                    .token("412563")
                    .build();
    }
}
