package com.lrnews.admin.service;

import com.lrnews.bo.NewAdminBO;
import com.lrnews.pojo.AdminUser;

public interface AdminUserService {

    AdminUser queryAdminUserByUsername(String username);

    AdminUser createAdminUser(NewAdminBO adminBO);
}
