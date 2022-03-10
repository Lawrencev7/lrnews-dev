package com.lrnews.vo;

import com.lrnews.pojo.Fans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FansGraphVO {
    Integer total;
    Integer maleCount;
    Integer femaleCount;
    Map<String, Integer> provinceCount;

    public static FansGraphVO generateFromFans(List<Fans> fansList) {
        FansGraphVO fansGraphVO = new FansGraphVO();
        fansGraphVO.setTotal(fansList.size());
        fansGraphVO.setFemaleCount((int) fansList.stream().filter(fans -> fans.getSex() == 0).count());
        fansGraphVO.setMaleCount(fansGraphVO.total - fansGraphVO.femaleCount);
        Map<String, Integer> provinceMap = new HashMap<>();
        fansList.forEach(fans -> {
            String pro = fans.getProvince();
            if (!provinceMap.containsKey(pro))
                provinceMap.put(pro, 1);
            else
                provinceMap.put(pro, provinceMap.get(pro) + 1);
        });
        fansGraphVO.provinceCount = provinceMap;
        return fansGraphVO;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(Integer maleCount) {
        this.maleCount = maleCount;
    }

    public Integer getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(Integer femaleCount) {
        this.femaleCount = femaleCount;
    }

}
