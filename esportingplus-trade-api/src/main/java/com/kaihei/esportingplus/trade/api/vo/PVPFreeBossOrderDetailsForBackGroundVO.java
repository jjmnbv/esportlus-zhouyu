package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangfang
 */
@Data
@ToString
@Builder
public class PVPFreeBossOrderDetailsForBackGroundVO implements Serializable {

    private static final long serialVersionUID = 4756036591062813059L;
    /**
     * 订单序列号
     */
    private String sequeue;
    /**
     * 车队序列号
     */
    private String teamSequeue;
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
    private Date updateCreate;
    /**
     * 段位名称
     */
    private String gameDanName;
    /**
     * 比赛结果
     */
    private List<PVPGameResult> gameResults;

    /**
     * 玩法模式, 0:上分; 1:陪玩;
     */
    private String playModeName;

    /**
     * 结算类型, 1:局; 2:小时
     */
    private String settlementTypeName;

    private String image;

    private List<PVPFreeOrderIncomeForBackGroundVO> baojiIncomes;


}
