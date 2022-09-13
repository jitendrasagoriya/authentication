package com.js.authentication.builder;

import com.js.authentication.model.Application;
import com.js.authentication.token.SecureTokenGenerator;

public class ApplicationBuilder {

    public static Application getApplication() {
        return new Application.ApplicationBuilder(
                SecureTokenGenerator.nextAppId("ANDOLA-UI"),
                "ANDOLA-UI",
                "ANDOLA-UI",
                SecureTokenGenerator.getToken()
        ).withSalt("MYSALT").build();
    }


}
