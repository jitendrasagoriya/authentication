package com.js.authentication.token;

import java.security.SecureRandom;

public class SecureTokenGenerator {

    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // 2048 bit keys should be secure until 2030 - https://web.archive.org/web/20170417095741/https://www.emc.com/emc-plus/rsa-labs/historical/twirl-and-rsa-key-size.htm
    public static final int SECURE_TOKEN_LENGTH = 256/8;

    public static final int SECURE_TOKEN_LENGTH_APP_ID = 256/24;

    private static final SecureRandom random = new SecureRandom();

    private static final char[] symbols = CHARACTERS.toCharArray();

    private static final char[] buf = new char[SECURE_TOKEN_LENGTH];

    private static final char[] bufAppId = new char[SECURE_TOKEN_LENGTH_APP_ID];

    /**
     * Generate the next secure random token in the series.
     */
    public static String nextToken() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static String nextAppId() {
        for (int idx = 0; idx < bufAppId.length; ++idx)
            bufAppId[idx] = symbols[random.nextInt(symbols.length)];
        return new String(bufAppId) +""+System.currentTimeMillis();
    }

    public static int getHash(String userName) {
        return userName.hashCode();
    }

    public static String getToken() {
        String token = nextToken() +""+System.currentTimeMillis();
        return token;
    }

    public static String nextAppId(String clientName) {
        return Base64BasicEncryption.encoding(clientName).replace("=", "").toUpperCase()+"" + nextAppId();
    }


}
