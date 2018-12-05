package com.kaihei.esportingplus.marketing.domian.service;

import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsDtlVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import java.util.Date;
import java.util.List;

/**
 * @author zl.zhao
 * @description:用户免费券管理接口
 * @date: 2018/11/19 14:43
 */
public interface MarketUserFreeCouponsService {
    /**
     * 获取免费上车次数
     *
     * @param uid
     * @return
     */

    /**
     * * 添加免费上车次数
     *
     * @param uid
     * @param freeCount
     * @param invalidTime
     * @param type 券码类型 1-免费车队
     * @param source 来源 0-未知 1-新版本登录任务
     * @return
     */
    Boolean addUserFreeCoupons(String uid, Integer freeCount, Date invalidTime, Integer type,
            Integer source);


    /**
     * 获取用户免单信息
     *
     * @param uid
     * @return
     */
    UserFreeCouponsInfoVo getUserFreeCouponsInfo(String uid);

    /**
     * 扣减免费次数
     * @param uid
     * @param reduceCount
     */
    UserFreeCouponsQueryResultVo reduceFreeCoupons(String uid, Integer reduceCount);

    /**
     * 返还免费次数券
     *
     * @param couponsIds
     */
    UserFreeCouponsQueryResultVo returnFreeCoupons(List<Long> couponsIds);


    /**
     * 清理已过期免费券至ES
     *
     * @param offset
     * @param limit
     */
    void cleanFreeCouponsExpired(Integer offset, Integer limit);

    /**
     * 查询匹配机会信息
     *
     */
    UserFreeCouponsDtlVo getUserFreeCouponsDetail();
}
