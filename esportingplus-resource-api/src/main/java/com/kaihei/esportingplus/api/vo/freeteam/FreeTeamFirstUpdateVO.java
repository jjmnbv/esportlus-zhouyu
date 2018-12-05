package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 免费车队首页弹窗
 * @author liangyi
 */
@Validated
@ApiModel(description = "免费车队首页弹窗")
@Builder
@AllArgsConstructor
public class FreeTeamFirstUpdateVO implements Serializable {

    private static final long serialVersionUID = 1068039729812764879L;

    /**
     * 首页弹窗图片
     */
    @NotEmpty
    @ApiModelProperty(value = "首页弹窗图片不能为空",
            required = true, position = 1, example = "https://www.baidu.com")
    private String alertUrl;

    /**
     * 首页弹窗开关
     */
    @NotNull
    @ApiModelProperty(value = "首页弹窗开关不能为空",
            required = true, position = 2, example = "1")
    private Integer alertSwitch;

    public FreeTeamFirstUpdateVO() {
    }

    public String getAlertUrl() {
        return alertUrl;
    }

    public void setAlertUrl(String alertUrl) {
        this.alertUrl = alertUrl;
    }

    public Integer getAlertSwitch() {
        return alertSwitch;
    }

    public void setAlertSwitch(Integer alertSwitch) {
        this.alertSwitch = alertSwitch;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}