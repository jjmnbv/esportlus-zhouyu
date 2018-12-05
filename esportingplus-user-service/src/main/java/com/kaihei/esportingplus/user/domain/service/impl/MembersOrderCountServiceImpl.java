package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.user.api.enums.UserDataEnum;
import com.kaihei.esportingplus.user.api.vo.UserOrderCountVo;
import com.kaihei.esportingplus.user.api.vo.UserOrderDateVo;
import com.kaihei.esportingplus.user.data.repository.MembersOrderCountRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersOrderCount;
import com.kaihei.esportingplus.user.domain.service.MembersOrderCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Auther: linruihe
 * @Date: 2018-12-01 18:14
 * @Description:
 */
@Component
public class MembersOrderCountServiceImpl implements MembersOrderCountService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MembersOrderCountRepository membersOrderCountRepository;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public MembersOrderCount getUserData(String uid,int orderType){
        MembersOrderCount membersOrderCount = membersOrderCountRepository.selectUserDateByUid(uid,orderType);
        membersOrderCount = updateOverTime(membersOrderCount);
        return membersOrderCount;
    }

    @Override
    public int inrcOrder(String uid,int orderType) {
        MembersOrderCount membersOrderCount = getUserData(uid,orderType);
        int result = 0;
        if(membersOrderCount != null){
            result = membersOrderCountRepository.updateOrderData(uid,orderType);
        }else{
            result = insertOrderCount(uid,(byte) orderType);
        }
        return result;
    }

    @Override
    public int inrcPlace(String uid,int orderType) {
        MembersOrderCount membersOrderCount = getUserData(uid,orderType);
        int result = 0;
        if(membersOrderCount != null){
            result = membersOrderCountRepository.updatePlaceData(uid,orderType);
        }else{
            result = insertOrderCount(uid,(byte) orderType);
        }
        return result;
    }

    @Override
    public UserOrderDateVo getUserOrderDate(String uid) {
        UserOrderDateVo userOrderDateVo = new UserOrderDateVo();
        UserOrderCountVo allOrderData = getAllOrderData(uid);
        UserOrderCountVo freeOrderData = getFreeOrderData(uid,UserDataEnum.USER_FREE.getCode());
        userOrderDateVo.setAllOrderData(allOrderData);
        userOrderDateVo.setFreeOrderData(freeOrderData);
        return userOrderDateVo;
    }

    @Override
    public UserOrderCountVo getUserTodayTotalData(String uid) {
        //查询uid所有过期的当天接单数和下单数记录，并进行处理
        List<MembersOrderCount> membersOrderCountList = membersOrderCountRepository.selectOverTimeTodayData(uid);
        for (MembersOrderCount membersOrderCount : membersOrderCountList) {
            updateOverTime(membersOrderCount);
        }
        return membersOrderCountRepository.selectSumByUid(uid);
    }

    @Override
    public void incrUserFreeData(List<String> acceptList, List<String> placeList) {
        for (String uid : acceptList) {
            inrcOrder(uid, UserDataEnum.USER_FREE.getCode());
        }
        for (String uid : placeList) {
            inrcPlace(uid,UserDataEnum.USER_FREE.getCode());
        }
    }

    /**
     * 处理过期的记录
     * @param membersOrderCount
     * @return
     */
    private MembersOrderCount updateOverTime(MembersOrderCount membersOrderCount){
        if(membersOrderCount != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date start = calendar.getTime();
            Byte todayPlaceTemp = null;
            Byte todayAcceptTemp = null;
            String uid = membersOrderCount.getUid();
            int orderType = membersOrderCount.getOrderType();
            if (membersOrderCount.getLastPlaceDatetime().getTime() > start.getTime()) {
                todayPlaceTemp = membersOrderCount.getTodayPlaceCount();
            }
            if (membersOrderCount.getLastAcceptDatetime().getTime() > start.getTime()) {
                todayAcceptTemp = membersOrderCount.getTodayAcceptCount();
            }
            if(todayPlaceTemp == null || todayAcceptTemp == null){
                todayPlaceTemp = todayPlaceTemp == null ? 0 : todayPlaceTemp;
                todayAcceptTemp = todayAcceptTemp == null ? 0 : todayAcceptTemp;
                membersOrderCountRepository.updateTodayData(uid,todayPlaceTemp,todayAcceptTemp,orderType);
                membersOrderCount.setTodayAcceptCount(todayAcceptTemp);
                membersOrderCount.setTodayPlaceCount(todayPlaceTemp);
            }
        }
        return membersOrderCount;
    }

    /**
     * 新增新记录到表中
     * @param uid
     * @param orderType
     * @return
     */
    private int insertOrderCount(String uid,byte orderType){
        MembersOrderCount orderCount = new MembersOrderCount();
        orderCount.setUid(uid);
        orderCount.setTodayPlaceCount((byte)1);
        orderCount.setTodayAcceptCount((byte)0);
        orderCount.setOrderType(orderType);
        orderCount.setTotalAcceptCount(0);
        orderCount.setTotalPlaceCount(1);
        orderCount.setLastAcceptDatetime(new Date());
        orderCount.setLastPlaceDatetime(new Date());
        return membersOrderCountRepository.insert(orderCount);
    }

    /**
     * 获取用户全部的接单和下单数据
     * @param uid
     * @return
     */
    private UserOrderCountVo getAllOrderData(String uid){
        UserOrderCountVo userOrderCountVo = getUserTodayTotalData(uid);
        return userOrderCountVo;
    }

    /**
     * 获取免费车队订单的数据
     * @param uid
     * @param orderType
     * @return
     */
    private UserOrderCountVo getFreeOrderData(String uid,int orderType){
        UserOrderCountVo userOrderCountVo = new UserOrderCountVo();
        userOrderCountVo.setUid(uid);
        int acceptCount = 0;
        int placeCount = 0;
        int todayAccept = 0;
        int todayPlace = 0;
        MembersOrderCount orderCount = getUserData(uid,orderType);
        if(orderCount != null){
            acceptCount = orderCount.getTotalAcceptCount();
            placeCount = orderCount.getTotalPlaceCount();
            todayAccept = orderCount.getTodayAcceptCount();
            todayPlace = orderCount.getTodayPlaceCount();
        }
        userOrderCountVo.setAcceptCount(acceptCount);
        userOrderCountVo.setPlaceCount(placeCount);
        userOrderCountVo.setTodayAccept(todayAccept);
        userOrderCountVo.setTodayPlace(todayPlace);
        return userOrderCountVo;
    }
}
