package com.spring.baseproject.utils.base;

import java.util.Base64;

public class Base64Utils {

    public static String decode(String base64EncodedString) {
        byte[] bytes = Base64.getDecoder().decode(base64EncodedString);
        return new String(bytes);
    }

    public static String encode(String value) {
        byte[] bytes = Base64.getEncoder().encode(value.getBytes());
        return new String(bytes);
    }
}
