package com.kaihei.esportingplus.riskrating.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

/**
 * 风控校验扩展参数
 * @author chenzhenjun
 */
@Validated
@ApiModel(value = "用户发起免费车队前校验", description = "用户发起免费车队前校验")
public class FreeTeamCheckParams extends FreeTeamBasicParams{

    @ApiModelProperty(value = "数美风险分", required = false, example = "200")
    private int riskScore;

    @ApiModelProperty(value = "数美风险详情", required = false, example = "")
    private String riskDetail;

    @ApiModelProperty(value = "用户昵称", required = false, example = "sandy")
    private String nickName;

    @ApiModelProperty(value = "鸡牌id", required = false, example = "111")
    private int chickenId;

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskDetail() {
        return riskDetail;
    }

    public void setRiskDetail(String riskDetail) {
        this.riskDetail = riskDetail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getChickenId() {
        return chickenId;
    }

    public void setChickenId(int chickenId) {
        this.chickenId = chickenId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
