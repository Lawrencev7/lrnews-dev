package com.lrnews.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomStringName {
    public static final int COMMON_USERNAME_LENGTH = 8;
    public static final int COMMON_FILENAME_LENGTH = 16;

    public static final String USER_NAME_PREFIX = "User-";
    public static final String ADMIN_NAME_PREFIX = "Admin-";
    public static final String FILE_NAME_PREFIX = "File-";
    public static final String ARTICLE_ID_PREFIX = "A";

    public static String getRandomUserName() {
        return getRandomName(USER_NAME_PREFIX);
    }

    public static String getRandomAdminName() {
        return getRandomName(ADMIN_NAME_PREFIX);
    }

    public static String getRandomFileName() {
        return getRandomName(FILE_NAME_PREFIX);
    }

    public static String getRandomArticleId() {
        return getRandomNumId(ARTICLE_ID_PREFIX);
    }

    @NotNull
    private static String getRandomName(String prefix) {
        StringBuilder val = new StringBuilder();
        val.append(prefix);
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

    @NotNull
    private static String getRandomNumId(String prefix) {
        StringBuilder val = new StringBuilder();
        val.append(prefix);
        Random random = new Random();

        for (int i = 0; i < COMMON_FILENAME_LENGTH; i++) {
            val.append(random.nextInt(10));
        }
        return val.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandomArticleId());
    }
}
