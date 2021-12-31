package com.lrnews.user.service.impl;

import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.enums.Gender;
import com.lrnews.enums.UserStatus;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AppUser;
import com.lrnews.user.mapper.AppUserMapper;
import com.lrnews.user.service.UserService;
import com.lrnews.utils.DateUtil;
import com.lrnews.utils.RandomStringName;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

import static com.lrnews.values.CommonValueStrings.DEFAULT_USER_AVATAR;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

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
        user.setNickname(RandomStringName.getRandomUserName());
        user.setFace(DEFAULT_USER_AVATAR);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Gender.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);
        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        boolean res = userMapper.insert(user) == 1;
        logger.info("createUser: with result: " + (res ? "succeed" : "failed"));
        return res ? user : null;
    }

    @Override
    public AppUser getUser(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional
    public void updateUserInfo(UpdateUserInfoBO userInfoBO) {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(userInfoBO, appUser);
        appUser.setUpdatedTime(new Date());
        appUser.setActiveStatus(UserStatus.ACTIVE.type);
        boolean res = userMapper.updateByPrimaryKeySelective(appUser) == 1;
        if (!res){
            CustomExceptionFactory.onException(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
    }
}
