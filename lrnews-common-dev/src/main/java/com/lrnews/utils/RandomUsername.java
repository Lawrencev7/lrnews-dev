package com.lrnews.utils;

import java.util.Random;

public class RandomUsername {
    public static final int COMMON_USERNAME_LENGTH = 8;

    public static String getRandomName(){
        StringBuilder val = new StringBuilder();
        val.append("User-");
        Random random = new Random();

        for(int i = 0; i < COMMON_USERNAME_LENGTH; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if( "char".equalsIgnoreCase(charOrNum) ) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else  {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }
}
