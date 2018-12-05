package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * @author Administrator
 */
@Validated
@ApiModel("成员被踢消息参数")
public class TeamImCmdMsgOutMemberParams extends TeamImCmdMsgBaseParams {

    private static final long serialVersionUID = 3058507186789832242L;

    /**
     * 被出原因  1, "老铁，你已经被踢出车队了！"  2, "老铁，由于您超时未支付已经被踢出车队了"
     */
    @ApiModelProperty(value = "被踢的原因类型", required = true, position = 1)
    @NotNull
    private Integer outReasonType;

    public Integer getOutReasonType() {
        return outReasonType;
    }

    public void setOutReasonType(Integer outReasonType) {
        this.outReasonType = outReasonType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
