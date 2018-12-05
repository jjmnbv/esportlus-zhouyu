package com.kaihei.esportingplus.user.data.manager;

/**
 * @author xiekeqing
 * @Title: MembersChickenCacheManager
 * @Description: TODO
 * @date 2018/9/1921:57
 */
public interface MembersChickenCacheManager {

    /**
     * 查询可用鸡牌号
     * <p>鸡牌使用redis的bitmap数据格式存储，已使用鸡牌号设置为1</p>
     * <p>查询可用鸡牌时从bitmap中获取大于10000000的最小未使用鸡牌号</p>
     */
    public String getAvailableChickenId();

}
