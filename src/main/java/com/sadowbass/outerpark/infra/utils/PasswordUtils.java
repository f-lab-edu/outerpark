package com.sadowbass.outerpark.infra.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class PasswordUtils {

    private static final int COST = 10;

    public static String encode(String originalPassword) {
        return encode(originalPassword, COST);
    }

    public static String encode(String originalPassword, int cost) {
        return BCrypt.withDefaults().hashToString(cost, originalPassword.toCharArray());
    }

    public static boolean verify(String originalPassword, String encodedPassword) {
        return BCrypt.verifyer().verify(originalPassword.toCharArray(), encodedPassword.toCharArray()).verified;
    }
}
