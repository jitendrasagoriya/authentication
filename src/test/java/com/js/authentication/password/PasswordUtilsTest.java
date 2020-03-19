package com.js.authentication.password;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordUtilsTest {

    @Test
    public void testPasswordPass() {
        String myPassword = "myPassword123";

        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

        // User provided password to validate
        String providedPassword = "myPassword123";

        boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, mySecurePassword, salt);

        assertThat(passwordMatch).isTrue();
    }
    @Test
    public void testPasswordFail() {
        String myPassword = "myPassword123";

        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

        // User provided password to validate
        String providedPassword = "myPassword1";

        boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, mySecurePassword, salt);

        assertThat(passwordMatch).isFalse();
    }


    @Test
    public void testGetPassword() {
        String myPassword = "myPassword123";

        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(10);

        System.out.println("salt :" +salt);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

        System.out.println("mySecurePassword :" +mySecurePassword);



    }
}
