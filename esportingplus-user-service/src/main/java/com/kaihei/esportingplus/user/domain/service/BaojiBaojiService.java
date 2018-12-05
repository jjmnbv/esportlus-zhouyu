package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.vo.PointDateVo;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoJiTag;

/**
 * @Description: 暴鸡信息服务
 * @Author: linruihe
 * @Date: 2018年11月15日
 */
public interface BaojiBaojiService {
    /**
     * 根据uid判断是否为暴娘
     * @param uid
     * @return
     */
    public boolean isBaoniang(String uid);

    /**
     * 根据userId和uid判断是否为暴鸡
     * @param userId
     * @param uid
     * @return
     */
    public boolean isBaoji(int userId,String uid);

    /**
     * 获取uid的身份
     * @param uid
     * @return
     */
    public int getIdentityByUid(String uid);

    /**
     * 获取暴鸡中心-鸡分数据
     * @param uid
     * @return
     */
    public PointDateVo getUserPointDate(String uid);

    /**
     * 获取暴鸡最高等级
     * @param uid
     * @return
     */
    public BaojiBaoJiTag getBaoJiMaxLevel(String uid);

    /**
     * 根据游戏Code的暴鸡等级
     * @param uid
     * @param gameCode
     * @return
     */
    public BaojiBaoJiTag getBaoJiLevelByGame(String uid, Integer gameCode);
}
