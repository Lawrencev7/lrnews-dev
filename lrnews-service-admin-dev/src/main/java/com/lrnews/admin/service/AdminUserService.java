package com.lrnews.admin.service;

import com.lrnews.bo.NewAdminBO;
import com.lrnews.pojo.AdminUser;

import java.util.List;

public interface AdminUserService {

    AdminUser queryAdminUserByUsername(String username);

    AdminUser createAdminUser(NewAdminBO adminBO);

    List<AdminUser> queryAdminListPageable(Integer page, Integer pageSize);
}
