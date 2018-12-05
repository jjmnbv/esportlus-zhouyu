package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangfang
 */
@Data
@ToString
@Builder
public class PVPFreeTeamMemberOrderVO implements Serializable {

    private static final long serialVersionUID = 1846371439173872839L;
    /**
     * 队员uid
     */
    private String uid;
    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 车队队员头像
     */
    private String avatar;
    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    private Byte userIdentity;
    /**
     *  订单状态 0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private String statusZh;
    /**
     * 暴鸡等级，只有队长和暴鸡才有，老板没有
     */
    private String baojiLevelZh;

    private Integer baojiLevel;
}
