package com.kaihei.esportingplus.marketing.domian.service.impl;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.marketing.BaseTest;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class MarketUserFreeCouponsServiceImplTest extends BaseTest {
    @Autowired
    private MarketUserFreeCouponsService marketUserFreeCouponsService;

    @Test
    public void incrCoupons() {
    }

    @Test
    public void assembleCouponsList() {
    }

    @Test
    public void addUserFreeCoupons() {
    }

    @Test
    public void getUserFreeCouponsInfo() {
    }

    @Test
    public void reduceFreeCoupons() {
        marketUserFreeCouponsService.reduceFreeCoupons("ea177747", 3);
    }

    @Test
    public void returnFreeCoupons() {
        List<Long> list = Lists.newArrayList();
        list.add(1292L);
        list.add(1241L);
        list.add(1243L);
        marketUserFreeCouponsService.returnFreeCoupons(list);

    }

    @Test
    public void cleanFreeCouponsExpired() {
    }

    @Test
    public void getUserFreeCouponsDetail() {
        marketUserFreeCouponsService.getUserFreeCouponsDetail();
    }
}