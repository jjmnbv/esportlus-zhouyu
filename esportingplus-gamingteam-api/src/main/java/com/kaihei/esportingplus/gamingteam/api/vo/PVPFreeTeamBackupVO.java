package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/** 后台系统免费订单VO
 * @author zhangfang
 */
@Data
@ToString
public class PVPFreeTeamBackupVO implements Serializable {

    private static final long serialVersionUID = 5854822519731355695L;
    /**
     * 车队序列号,通过分布式id生成器生成
     */
    private String sequence;


    /**
     * 房间标题,20个字符以内
     */
    private String title;
    /**
     * 结算类型, 1:局; 2:小时
     */
    private Byte settlementType;

    /**
     * 结算数量(保留1位有效数字)
     */
    private BigDecimal settlementNumber;
    /**
     * 结算名称
     */
    private String settlementName;

    /**
     * 免费车队类型显示名称
     */
    private String freeTeamTypeName;


    /**
     * 车队所属游戏大区名称
     */
    private String gameZoneName;

    /**
     * 段位下限名称
     */
    private String lowerDanName;

    /**
     * 段位上限名称
     */
    private String upperDanName;

    /**
     * 车队队员UID
     */
    private String uid;

    /**
     * 车队队员昵称
     */
    private String username;
    /**
     * 鸡牌号
     */
    private String chickenId;
    /**
     * 车队状态中文描述
     */
    private String statusZh;
    private Integer status;
    /**
     * 开车时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 比赛结果
     */
    private List<TeamGameResultVO> gameResults;

}
