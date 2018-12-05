package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * 订单详情
 * @author zhangfang
 */
@Data
@ToString
public class PVPFreeTeamOrderVo implements Serializable{

    private static final long serialVersionUID = 7229551905575162805L;
    /**
     * 订单号
     */
    private String sequence;
    /**
     * 车队订单号
     */
    private String teamSequence;
    /**
     * 订单信息中文描述
     */
    private String orderInfoZh;
    /**
     * 订单状态
     */
    private Byte status;
    /**
     * 订单状态中文描述
     */
    private String statusZh;
    /**
     * 比赛结果中文描述
     */
    private String  gameResultZh;
    /**
     * 查询者身份 用它来判定是否有积分收益
     */
    private Byte userIdentity;

    /**
     * 获得的总积分，当身份是暴鸡暴娘时有值
     */
    private Integer amount;
    /**
     * 其它人订单
     */
    private List<PVPFreeTeamMemberOrderVO> orders;
    /**
     * 老板给与的积分
     */
    private List<PVPFreeBossPointsVO> bossPoints;


}
