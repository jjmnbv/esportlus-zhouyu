package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;

/**
 * 老板创建订单时需要的 VO 对象
 * @author liangyi
 */
@Data
@ToString
public class PVPBossInfoForOrderVO implements Serializable {

    private static final long serialVersionUID = 2588485884118612868L;

    /** 车队序列号 */
    private String sequence;

    /** 游戏 code */
    private Integer gameId;

    /** 游戏 code */
    private String gameName;

    /** 游戏大区ID*/
    private Integer gameZoneId;
    /**
     * 鸡牌号
     */
    private String userChickenId;
    /**
     * 用户昵称
     */
    private String userNickname;

    /** 游戏大区名称 */
    private String gameZoneName;

    /** 段位ID code */
    private Integer gameDanId;

    /** 段位名称 */
    private String gameDanName;

    /** 玩法模式, 0:上分; 1:陪玩; */
    private Byte playMode;

    /** 结算类型, 1:局; 2:小时 */
    private Byte settlementType;
    /** 结算数量(保留1位有效数字) */
    private BigDecimal settlementNumber;

    /** 老板支付金额(即车队折扣价) */
    private Integer price;

    /**
     * 车队队员UID
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
     * 车队队员状态(0: 待支付(只针对老板), 1: 准备入团, 2: 已入团)
     */
    private Integer status;


}
