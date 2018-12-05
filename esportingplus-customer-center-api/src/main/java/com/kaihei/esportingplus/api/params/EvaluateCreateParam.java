package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.api.enums.EvaluateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 查询评价列表参数
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@ApiModel("评价订单创建请求参数")
public final class EvaluateCreateParam implements Serializable {

    private static final long serialVersionUID = 5938952013589345198L;

    @NotEmpty(message = "订单id不能为空")
    @ApiModelProperty(value = "订单id", required = true, position = 1, example = "13245")
    private Integer orderId;

    @NotEmpty(message = "评星等级不能为空")
    @ApiModelProperty(value = "评星等级", required = true, position = 2, example = "4")
    private short star;

    @NotEmpty(message = "评价内容不能为空")
    @ApiModelProperty(value = "评价内容", required = true, position = 3, example = "6得飞起")
    private String content;

    @NotEmpty(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型", required = true, position = 4, example = "1")
    private EvaluateTypeEnum orderType;

    @NotEmpty(message = "暴鸡id")
    @ApiModelProperty(value = "暴鸡id", required = true, position = 5, example = "45781")
    private String baojiUid;

    @NotEmpty(message = "暴鸡类型")
    @ApiModelProperty(value = "暴鸡类型", required = true, position = 6, example = "BJ")
    private String baojiType;

    @NotEmpty(message = "暴鸡级别")
    @ApiModelProperty(value = "暴鸡级别", required = true, position = 7, example = "100")
    private short baojiLevel;


    @NotEmpty(message = "订单完成时间")
    @ApiModelProperty(value = "订单完成时间", required = true, position = 8, example = "2018-11-19 20:40:01")
    private String orderFinishTime;

    @NotEmpty(message = "游戏局数")
    @ApiModelProperty(value = "游戏局数", required = true, position = 9, example = "3")
    private short gameRound;

    @NotEmpty(message = "比赛结果")
    @ApiModelProperty(value = "比赛结果", required = true, position = 10, example = "0,1,1")
    private String gameResult;

    @NotEmpty(message = "车队单老板上车段位")
    @ApiModelProperty(value = "车队单老板上车段位", required = false, position = 11, example = "20")
    private String laobanDan;

    @NotEmpty(message = "车队类型id")
    @ApiModelProperty(value = "车队类型id", required = false, position = 12, example = "1")
    private String premadeTypeId;

    @NotEmpty(message = "车队slug")
    @ApiModelProperty(value = "车队slug", required = false, position = 13, example = "1325a7b5")
    private String premadeSlug;

    @NotEmpty(message = "车队id")
    @ApiModelProperty(value = "车队id", required = false, position = 14, example = "1324")
    private int premadeId;

    @NotEmpty(message = "自定义技能id")
    @ApiModelProperty(value = "自定义技能id", required = false, position = 15, example = "1324")
    private int customSkillId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public short getStar() {
        return star;
    }

    public void setStar(short star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EvaluateTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(EvaluateTypeEnum orderType) {
        this.orderType = orderType;
    }

    public String getBaojiUid() {
        return baojiUid;
    }

    public void setBaojiUid(String baojiUid) {
        this.baojiUid = baojiUid;
    }

    public String getBaojiType() {
        return baojiType;
    }

    public void setBaojiType(String baojiType) {
        this.baojiType = baojiType;
    }

    public short getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(short baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public String getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(String orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public short getGameRound() {
        return gameRound;
    }

    public void setGameRound(short gameRound) {
        this.gameRound = gameRound;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public String getLaobanDan() {
        return laobanDan;
    }

    public void setLaobanDan(String laobanDan) {
        this.laobanDan = laobanDan;
    }

    public String getPremadeTypeId() {
        return premadeTypeId;
    }

    public void setPremadeTypeId(String premadeTypeId) {
        this.premadeTypeId = premadeTypeId;
    }

    public String getPremadeSlug() {
        return premadeSlug;
    }

    public void setPremadeSlug(String premadeSlug) {
        this.premadeSlug = premadeSlug;
    }

    public int getPremadeId() {
        return premadeId;
    }

    public void setPremadeId(int premadeId) {
        this.premadeId = premadeId;
    }

    public int getCustomSkillId() {
        return customSkillId;
    }

    public void setCustomSkillId(int customSkillId) {
        this.customSkillId = customSkillId;
    }
}
