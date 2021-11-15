package com.lrnews.user.service.impl;

import com.lrnews.enums.Gender;
import com.lrnews.enums.UserStatus;
import com.lrnews.pojo.AppUser;
import com.lrnews.user.mapper.AppUserMapper;
import com.lrnews.user.service.UserService;
import com.lrnews.utils.DateUtil;
import com.lrnews.utils.RandomUsername;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    public final AppUserMapper userMapper;

    public UserServiceImpl(AppUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public AppUser queryUserExist(String phone) {
        Example userExample = new Example(AppUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile", phone);
        return userMapper.selectOneByExample(userExample);
    }

    @Transactional
    @Override
    public AppUser createUser(String userPhone) {
        AppUser user = new AppUser();
        user.setId(Sid.nextShort());
        user.setMobile(userPhone);
        user.setNickname(RandomUsername.getRandomName());
        user.setFace("");
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Gender.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);
        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        return userMapper.insert(user) == 1 ? user : null;
    }
}
