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
public class PVPFreeBossOrderForBackGroundVO implements Serializable {

    private static final long serialVersionUID = 4756036591062813059L;
    /**
     * 订单序列号
     */
    private String sequence;
    /**
     * 车队序列号
     */
    private String teamSequence;
    /**
     * 免费车队类型名称
     */
    private String freeTeamTypeTame;

    /**
     * 老板uid
     */
    private String uid;
    /**
     * 老板鸡牌号
     */
    private String chickenId;
    /**
     * 老板昵称
     */
    private String nickname;
    /**
     * 用户身份
     */
    private Integer userIdentity;
    /**
     * 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    private Integer userBaojiLevel;

    /**
     * 免费车队订单状态
     */
    private Integer status;
    /**
     * 给与的总积分
     */
    private Integer amount;
    /**
     * 订单创建时间
     */
    private Date gmtCreate;
    /**
     * 订单修改时间
     */
    private Date gmtModified;
}
