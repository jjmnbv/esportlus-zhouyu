package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.vo.UserOrderCountVo;
import com.kaihei.esportingplus.user.api.vo.UserOrderDateVo;
import com.kaihei.esportingplus.user.domain.entity.MembersOrderCount;

import java.util.List;

/**
 * @Auther: linruihe
 * @Date: 2018-12-01 18:13
 * @Description:
 */
public interface MembersOrderCountService {
    public MembersOrderCount getUserData(String uid,int orderType);

    public int inrcOrder(String uid,int orderType);

    public int inrcPlace(String uid,int orderType);

    public UserOrderDateVo getUserOrderDate(String uid);

    public UserOrderCountVo getUserTodayTotalData(String uid);

    public void incrUserFreeData(List<String> acceptList, List<String> placeList);
}
