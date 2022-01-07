package com.lrnews.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.admin.mapper.AdminUserMapper;
import com.lrnews.admin.service.AdminUserService;
import com.lrnews.bo.NewAdminBO;
import com.lrnews.pojo.AdminUser;
import com.lrnews.utils.RandomStringName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserService.class);

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

    @Override
    @Transactional
    public AdminUser createAdminUser(NewAdminBO adminBO) {
        String adminId = RandomStringName.getRandomUserName();

        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(adminBO.getUsername());
        adminUser.setAdminName(adminBO.getAdminName());
        if(StringUtils.isNotBlank(adminBO.getPassword())){
            adminUser.setPassword(BCrypt.hashpw(adminBO.getPassword(), BCrypt.gensalt()));
        }else if(StringUtils.isNotBlank(adminBO.getFaceId())){
            adminUser.setFaceId(adminBO.getFaceId());
        }

        boolean res = adminUserMapper.insert(adminUser) == 1;
        logger.info("createAdminUser: with result: " + (res ? "succeed" : "failed"));
        return res ? adminUser : null;
    }

    @Override
    public List<AdminUser> queryAdminListPageable(Integer page, Integer pageSize) {
        Example admin = new Example(AdminUser.class);
        admin.orderBy("createdTime").desc();

        PageHelper.startPage(page, pageSize);

        return adminUserMapper.selectByExample(admin);
    }
}
