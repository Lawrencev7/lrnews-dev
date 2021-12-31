package com.lrnews.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomStringName {
    public static final int COMMON_USERNAME_LENGTH = 8;
    public static final int COMMON_FILENAME_LENGTH = 16;

    public static String getRandomUserName() {
        StringBuilder val = new StringBuilder();
        val.append("User-");
        return getRandomName(val);
    }

    public static String getRandomFileName() {
        StringBuilder val = new StringBuilder();
        val.append("File-");
        return getRandomName(val);
    }

    @NotNull
    private static String getRandomName(StringBuilder val) {
        Random random = new Random();

        for (int i = 0; i < COMMON_FILENAME_LENGTH; i++) {
            boolean isChar = random.nextInt(2) % 2 == 0;
            if (isChar) {
                val.append((char) (random.nextInt(26) + 65));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }
}
