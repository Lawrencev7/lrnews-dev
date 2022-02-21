package com.lrnews.user.service;

import com.lrnews.vo.PagedGridVO;

import java.util.Date;

public interface UserMngService {
    PagedGridVO queryAllUserList(String nickname, Integer status, Date startDate,
                                 Date endDate, Integer page, Integer pageSize);

    void setUserStatus(String userId, Integer status);
}
