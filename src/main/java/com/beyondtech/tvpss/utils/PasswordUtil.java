package com.beyondtech.tvpss.utils;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?/~";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length, boolean includeSpecialChars) {
        StringBuilder passwordBuilder = new StringBuilder();

        StringBuilder allowedChars = new StringBuilder(LOWERCASE);
        allowedChars.append(UPPERCASE).append(DIGITS);

        if (includeSpecialChars) {
            allowedChars.append(SPECIAL_CHARACTERS);
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            passwordBuilder.append(allowedChars.charAt(randomIndex));
        }

        return passwordBuilder.toString();
    }

    public static String generateSimplePassword(String base) {
        return base + random.nextInt(100);
    }

    public static String generateCustomPatternPassword(int length) {
        StringBuilder passwordBuilder = new StringBuilder();
        String allowedChars = LOWERCASE + DIGITS;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            passwordBuilder.append(allowedChars.charAt(randomIndex));
        }

        return passwordBuilder.toString();
    }
}
