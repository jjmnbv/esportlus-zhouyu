package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "RPG订单队员列表参数", description = "RPG订单队员列表参数")
public final class OrderTeamRPGMember extends OrderTeamMember implements Serializable {


    private static final long serialVersionUID = 4720856644309084337L;

    @NotNull(message = "暴鸡所在副本位置")
    @Range(min = 1,max = 2,message = "暴鸡所在副本位置，要求：1-2")
    @ApiModelProperty(value = "暴鸡所在位置:副本位置code,1: dps 2:辅助")
    private Integer raidLocation;

    public Integer getRaidLocation() {
        return raidLocation;
    }

    public void setRaidLocation(Integer raidLocation) {
        this.raidLocation = raidLocation;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
