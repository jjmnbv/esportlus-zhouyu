package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.esportingplus.user.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiekeqing
 * @Title: MembersChickenCacheManagerTest
 * @Description: TODO
 * @date 2018/9/2015:27
 */
public class MembersChickenCacheManagerTest extends BaseTest {

    @Autowired
    private MembersChickenCacheManager membersChickenCacheManager;

    @Test
    public void testGetAvailableChickenId(){
        String chickenId = membersChickenCacheManager.getAvailableChickenId();
        System.out.println(chickenId);
        Assert.assertNotNull(chickenId);
    }

}
