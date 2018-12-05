package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.kaihei.esportingplus.common.data.Castable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class PVPRedisTeamVO implements Castable {

    /**
     * 车队唯一标识符
     */
    private Long id;

    /**
     * 车队序列号,通过分布式id生成器生成
     */
    private String sequence;

    /**
     * 车队房间编号，1000-9999 ，号码随机赋予不重复
     */
    private Integer roomNum;

    /**
     * 房间标题,20个字符以内
     */
    private String title;

    /**
     * 车队类型, -1: 未知; 0:免费; 1:付费;
     */
    private Byte teamType;

    /**
     * 玩法模式, 0:上分; 1:陪玩;
     */
    private Byte playMode;

    /**
     * 结算类型, 1:局; 2:小时
     */
    private Byte settlementType;

    /**
     * 结算数量(保留1位有效数字)
     */
    private BigDecimal settlementNumber;

    /**
     * 车队原始位置数
     */
    private Integer originalPositionCount;

    /**
     * 车队实际位置数
     */
    private Integer actuallyPositionCount;

    /**
     * 车队状态，0：准备中 1：已发车 2：已解散 3：已结束
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 车队ID
     */
    private Long teamId;

    /**
     * 车队所属游戏ID
     */
    private Integer gameId;

    /**
     * 车队所属游戏名称
     */
    private String gameName;

    /**
     * 车队所属游戏大区ID
     */
    private Integer gameZoneId;

    /**
     * 车队所属游戏大区名称
     */
    private String gameZoneName;

    /**
     * 段位下限ID
     */
    private Integer lowerDanId;

    /**
     * 段位下限名称
     */
    private String lowerDanName;

    /**
     * 段位上限ID
     */
    private Integer upperDanId;

    /**
     * 段位上限名称
     */
    private String upperDanName;

    /**
     * 车队初始价格(按暴鸡队长的算), 单位: 分
     */
    private Integer initFee;

    /**
     * 车队折扣价, 单位: 分
     */
    private Integer discountFee;

    /**
     * 老板支付超时时间(秒)
     */
    private Integer paymentTimeout;
}
