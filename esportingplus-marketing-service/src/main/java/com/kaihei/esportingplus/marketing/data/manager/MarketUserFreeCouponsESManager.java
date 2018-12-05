package com.kaihei.esportingplus.marketing.data.manager;

import com.kaihei.esportingplus.marketing.domian.entity.MarketUserFreeCoupons;
import java.util.List;
import java.util.Map;

/**
 * @author zl.zhao
 * @description:免费次数券ES管理
 * @date: 2018/11/20 15:26
 */
public interface MarketUserFreeCouponsESManager {
    /**
     * 根据用户uid和券码id查询次数信息
     *
     * @param couponsId 次数券id集合
     */
    public Map<String, Object> getUserFreeCouponsES(Long couponsId);

    /**
     * 删除返还券码信息
     *
     * @param couponsId 次数券id集合
     */
    public void delUserFreeCouponsES(Long couponsId);

    /**
     * 保存免费券信息到es
     *
     * @param coupons 免费券信息集合
     * @param dataType 数据来源(1-已使用 2-已过期)
     */
    public void saveUserFreeCouponsES(List<MarketUserFreeCoupons> coupons, String dataType);

    /**
     * 查询已使用的累计信息
     *
     * @param value
     * @return
     */
    public Integer termQuery(String value);

    /**
     * 批量查询
     *
     * @param couponsIds 次数券id集合
     * @return
     */
    List<MarketUserFreeCoupons>  getUserFreeCouponsESByBatch(List<Long> couponsIds);

    /**
     * 批量删除
     *
     * @param couponsIds 次数券id集合
     * @return
     */
    void delUserFreeCouponsESByBatch(List<Long> couponsIds);
}
