package com.lrnews.user.service;

import com.lrnews.pojo.AppUser;
import com.lrnews.pojo.Fans;
import com.lrnews.vo.PagedGridVO;

import java.util.List;

public interface FansService {
    Boolean isFollowedBy(String writerId, String fansId);

    Boolean follow(String writerId, AppUser fan);

    Boolean unfollow(String writerId, AppUser fan);

    PagedGridVO queryMyFans(String writerId, Integer page, Integer pageSize);

    Integer countMyFans(String writerId);

    Integer countMySubscribe(String writeId);

    List<Fans> getAllFans(String id);
}
