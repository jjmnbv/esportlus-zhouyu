package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "PVP订单队员列表参数", description = "PVP订单队员列表参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class OrderTeamPVPMember extends OrderTeamMember implements Serializable {

    private static final long serialVersionUID = 196921721523226186L;

    @NotNull(message = "老板段位ID不能为空，暴鸡填0")
    @ApiModelProperty(value = "老板段位ID：预计收益需要")
    private Integer bossDanId;

    public Integer getBossDanId() {
        return bossDanId;
    }

    public void setBossDanId(Integer bossDanId) {
        this.bossDanId = bossDanId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
