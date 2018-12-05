package com.kaihei.esportingplus.marketing.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserFreeCoupons;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MarketUserFreeCouponsRepository extends CommonRepository<MarketUserFreeCoupons> {
    /**
     * 根据用户uID查询免费券
     *
     * @param uid 用户ID
     * @return List<MarketUserFreeCoupons>
     */
    public List<MarketUserFreeCoupons> selectByUid(String uid);

    /**
     * 根据免费券id集合删除免费券
     *
     * @param ids 免费券id集合
     */
    public int deleteByIds(@Param("ids") List<Long> ids);


    /**
     * 增加用户免费次数券，若记录不存在则插入新记录
     *
     * @param couponsList 用户免费次数券对象集合
     */
    public void insertAndUpdate(List<MarketUserFreeCoupons> couponsList);

    /**
     * 查询已过期券
     *
     * @return List<MarketUserFreeCoupons>
     */
    public List<MarketUserFreeCoupons> selectListByExpired();

    /**
     * 统计已过期券
     *
     * @return Integer
     */
    public Integer selectListByExpiredCount();

    /**
     * 批量插入免费次数券
     *
     * @return Integer
     */
    public Integer insertBatch(List<MarketUserFreeCoupons> couponsList);

    /**
     * 按来源统计次数券
     *
     * @return Integer
     */
    public Integer selectByUidAndSource(@Param("uid") String uid, @Param("source") Integer source);
}