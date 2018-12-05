package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangfang
 */
@Data
@ToString
public class PVPTeamMemberBaseOrderVO implements Serializable {

    private static final long serialVersionUID = -5136053054316694860L;
    /**
     * 支出或者收益
     */
    private Integer amount;

    private String uid;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 是否是队长
     */
    private boolean isLeader;
    /**
     * y用户身份
     */
    private Byte userIdentity;
    /**
     * 暴鸡等级，只有队长和暴鸡才有，老板没有
     */
    private Integer baojiLevel;
    /**
     * 暴鸡中文
     */
    private String baojiLevelZh;

    /**
     * 车队队员头像
     */
    private String avatar;
    /**
     *  下单或者接单中文
     */
    private String orderCountZh;

    public boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }
}
