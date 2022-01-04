package com.lrnews.admin.service.impl;

import com.lrnews.admin.mapper.AdminUserMapper;
import com.lrnews.admin.service.AdminUserService;
import com.lrnews.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    final AdminUserMapper adminUserMapper;

    public AdminUserServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public AdminUser queryAdminUserByUsername(String username) {

        Example admin = new Example(AdminUser.class);
        Example.Criteria criteria = admin.createCriteria();
        criteria.andEqualTo("username", username);

        return adminUserMapper.selectOneByExample(admin);
    }
}
