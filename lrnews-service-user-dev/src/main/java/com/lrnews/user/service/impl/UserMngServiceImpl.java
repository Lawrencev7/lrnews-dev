package com.lrnews.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.enums.UserStatus;
import com.lrnews.pojo.AppUser;
import com.lrnews.user.mapper.AppUserMapper;
import com.lrnews.user.service.UserMngService;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserMngServiceImpl implements UserMngService {

    private final Logger logger = LoggerFactory.getLogger(UserMngService.class);

    private final AppUserMapper userMapper;

    public UserMngServiceImpl(AppUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PagedGridVO queryAllUserList(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }

        if (UserStatus.isUserStatusValid(status)){
            criteria.andEqualTo("activeStatus", status);
        }

        if(startDate != null && endDate != null){
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
            criteria.andLessThanOrEqualTo("updatedTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        return PagedGridVO.getPagedGrid(userMapper.selectByExample(example), page);
    }

    @Override
    public void setUserStatus(String userId, Integer status) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(status);
        logger.info("Set user {} active status: {}", userId, status);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
