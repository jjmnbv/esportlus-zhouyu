package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 创建老板订单参数
 * @author zhangfang
 */
@ApiModel("查询订单列表请求参数")
@Validated
public class PVPBossOrderCreateParams implements Serializable{

    private static final long serialVersionUID = 8026772783125167651L;
    @NotBlank(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true, position = 1, example = "223867704556126208")
    private String teamSequence;

    @ApiModelProperty(value = "优惠券id(可选)", required = false, position = 2, example = "[0]")
    private List<Long> couponId;

    @NotBlank(message = "appId类型不能为空")
    @ApiModelProperty(value = "appId类型", required = true, position = 1, example = "223867704556126208")
    private String appId;

    @NotBlank(message = "支付渠道标签不能为空")
    @ApiModelProperty(value = "支付渠道标签", required = true, position = 1, example = "223867704556126208")
    private String channerTag;


    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public List<Long> getCouponId() {
        return couponId;
    }

    public void setCouponId(List<Long> couponId) {
        this.couponId = couponId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannerTag() {
        return channerTag;
    }

    public void setChannerTag(String channerTag) {
        this.channerTag = channerTag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
