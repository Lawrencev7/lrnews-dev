package com.lrnews.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.pojo.AppUser;
import com.lrnews.pojo.Fans;
import com.lrnews.user.mapper.FansMapper;
import com.lrnews.user.service.FansService;
import com.lrnews.vo.PagedGridVO;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FansServiceImpl implements FansService {

    private final FansMapper fansMapper;

    public FansServiceImpl(FansMapper fansMapper) {
        this.fansMapper = fansMapper;
    }

    @Override
    public Boolean isFollowedBy(String writerId, String fansId) {
        Fans fans = new Fans();
        fans.setFanId(fansId);
        fans.setWriterId(writerId);

        // This will be optimized in the future
        int count = fansMapper.selectCount(fans);

        return count > 0;
    }

    @Override
    public Boolean follow(String writerId, AppUser fanUser) {
        Fans fan = new Fans();
        fan.setId(Sid.nextShort());
        fan.setFanId(fanUser.getId());
        fan.setWriterId(writerId);

        fan.setFace(fanUser.getFace());
        fan.setFanNickname(fanUser.getNickname());
        fan.setSex(fanUser.getSex());
        fan.setProvince(fanUser.getProvince());

        int res = fansMapper.insert(fan);

        return res == 1;
    }

    @Override
    @Transactional
    public Boolean unfollow(String writerId, AppUser fanUser) {
        Fans fan = new Fans();
        fan.setFanId(fanUser.getId());
        fan.setWriterId(writerId);

        int delete = fansMapper.delete(fan);

        return delete == 1;
    }

    @Override
    public PagedGridVO queryMyFans(String writerId, Integer page, Integer pageSize) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        PageHelper.startPage(page, pageSize);
        List<Fans> fansList = fansMapper.select(fans);
        return PagedGridVO.getPagedGrid(fansList, page);
    }

    @Override
    public Integer countMyFans(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        return fansMapper.selectCount(fans);
    }

    @Override
    public Integer countMySubscribe(String id) {
        Fans fans = new Fans();
        fans.setFanId(id);
        return fansMapper.selectCount(fans);
    }

    @Override
    public List<Fans> getAllFans(String id) {
        Fans fans = new Fans();
        fans.setWriterId(id);
        return fansMapper.select(fans);
    }
}
