package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsQueryVo;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsVo;
import com.kaihei.esportingplus.user.api.vo.UserPointQueryVo;
import com.kaihei.esportingplus.user.domain.entity.MembersUserPointItem;

/**
 * 用户鸡分服务类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 14:21
 */
public interface MembersUserPointService {


    /**
     * 给指定用户增加鸡分
     *
     * @param userId 用户ID
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param operationUserId 积分来源操作用户ID
     * @return boolean
     */
    public MembersUserPointItem incrPoint(Integer userId, Integer incrPointAmount, Integer itemType,
                                          Integer operationUserId, String slug);

    /**
     * 给指定用户增加鸡分
     *
     * @param userId 用户ID
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param slug
     * @return boolean
     */
    public Integer incrPoint(Integer userId, Integer incrPointAmount, Integer itemType, String slug);

    /**
     * 查询用户鸡分
     *
     * @param uid 用户uid
     * @return UserPointQueryVo
     */
    public UserPointQueryVo queryUserPoint(String uid);

    /**
     * 分页查询用户鸡分明细
     *
     * @param uid uid
     * @param pagingRequest 分页参数
     * @return PagingResponse<UserPointItemsQueryVo>
     */
    public PagingResponse<UserPointItemsQueryVo> listUserPointItems(String uid,
            PagingRequest pagingRequest);

    /**
     * 鸡分兑换暴鸡值
     *
     * @param uid uid
     * @param exchangeAmount 需兑换的暴鸡值
     * @return boolean
     */
    public boolean exchangeScore(String uid, Integer exchangeAmount) throws BusinessException;


    /**
     * 根据车队标识查询车队获得的积分
     *
     * @param itemType 鸡分来源类型
     * @param slug 车队标识符
     * @return PagingResponse<UserPointItemsQueryVo>
     */
    public UserPointItemsQueryVo getUserPointItem(Integer itemType, String slug);


    /**
     * 统计积分累计信息
     *
     * @param uid 用户uid
     */
    public UserPointItemsVo getUserAccumulatePoint(String uid);
}
