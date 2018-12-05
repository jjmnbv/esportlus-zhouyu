package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangfang
 */
@Data
@ToString
public class PVPFreeOrderIncomeForBackGroundVO implements Serializable{

    private static final long serialVersionUID = 1005347817958561306L;
    //detail.income
    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    private String userIdentity;
    /**
     * 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    private String userBaojiLevel;
    /**
     * uid
     */
    private String uid;
    /**
     * 鸡牌号
     */
    private String chickenId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 收益或者贡献
     */
    private Integer income;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 时间
     */
    private Date gmtUpdate;

}
