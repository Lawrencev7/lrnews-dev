package com.lrnews.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AdminPwdUtil {
    public static String encode(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        System.out.println(encode("123456"));
    }
}
