package com.lrnews.admin.service;

import com.lrnews.bo.AdminBO;
import com.lrnews.pojo.AdminUser;
import com.lrnews.vo.PagedGridVO;

public interface AdminUserService {

    AdminUser queryAdminUserByUsername(String username);

    AdminUser createAdminUser(AdminBO adminBO);

    PagedGridVO queryAdminListPageable(Integer page, Integer pageSize);
}
