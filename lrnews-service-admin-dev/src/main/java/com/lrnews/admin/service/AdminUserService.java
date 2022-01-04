package com.lrnews.admin.service;

import com.lrnews.pojo.AdminUser;

public interface AdminUserService {

    AdminUser queryAdminUserByUsername(String username);
}
