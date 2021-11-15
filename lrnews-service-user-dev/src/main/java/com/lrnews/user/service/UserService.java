package com.lrnews.user.service;

import com.lrnews.pojo.AppUser;

public interface UserService {
    AppUser queryUserExist(String phone);

    AppUser createUser(String phone);
}
