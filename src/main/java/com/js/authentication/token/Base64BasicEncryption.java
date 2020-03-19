package com.js.authentication.token;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64BasicEncryption {
    static Base64.Encoder encoder = Base64.getEncoder();

    static Base64.Decoder decoder = Base64.getDecoder();

    public static String  encoding(String charaters) {
        return encoder.encodeToString(charaters.getBytes(StandardCharsets.UTF_8));
    }

    public static String  decoding(String charaters) {
        return new String(decoder.decode(charaters), Charset.forName("UTF-8"));
    }
}
