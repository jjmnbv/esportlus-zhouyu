package com.kaihei.esportingplus.marketing.data.manager;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.marketing.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class MarketUserFreeCouponsESManagerImplTest extends BaseTest {

    @Autowired
    private MarketUserFreeCouponsESManager marketUserFreeCouponsESManager;


    @Test
    public void getUserFreeCouponsESByBatch() {
        List<Long> list = Lists.newArrayList();
        for(int i=0; i<10; i++){
            list.add(Long.valueOf(i));
        }
        list.add(1292L);
        list.add(1241L);
        list.add(1243L);
        marketUserFreeCouponsESManager.getUserFreeCouponsESByBatch(list);
    }

    @Test
    public void delUserFreeCouponsESByBatch() {
        List<Long> list = Lists.newArrayList();
        for(int i=0; i<10; i++){
            list.add(Long.valueOf(i));
        }
//        list.add(1292L);
//        list.add(1241L);
//        list.add(1243L);
        marketUserFreeCouponsESManager.delUserFreeCouponsESByBatch(list);
    }
}