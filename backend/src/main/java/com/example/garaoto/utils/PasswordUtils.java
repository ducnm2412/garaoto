package com.example.garaoto.utils;

import java.security.SecureRandom;

public final class PasswordUtils {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtils() {
    }

    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUpper && hasLower && hasDigit;
    }

    public static String generateRandomPassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Độ dài mật khẩu phải từ 6 ký tự trở lên");
        }

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }
}