package com.lrnews.user.service;

import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.pojo.AppUser;

public interface UserService {
    AppUser queryUserExist(String phone);

    AppUser createUser(String phone);

    AppUser getUser(String userId);

    void updateUserInfo(UpdateUserInfoBO userInfoBO);
}
