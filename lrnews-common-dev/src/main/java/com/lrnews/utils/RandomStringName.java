package com.lrnews.utils;

import java.util.Random;

public class RandomStringName {
    public static final int COMMON_USERNAME_LENGTH = 8;

    public static String getRandomName() {
        StringBuilder val = new StringBuilder();
        val.append("User-");
        Random random = new Random();

        for (int i = 0; i < COMMON_USERNAME_LENGTH; i++) {
            boolean isChar = random.nextInt(2) % 2 == 0;
            if (isChar) {
                val.append((char) (random.nextInt(26) + 65));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandomName());
    }
}
