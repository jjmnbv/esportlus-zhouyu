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
 * 免费车队匹配等待配置参数
 * @author liangyi
 */
@Validated
@ApiModel(description = "免费车队匹配等待配置")
@Builder
@AllArgsConstructor
public class FreeTeamMatchingWaitVO implements Serializable {

    private static final long serialVersionUID = -7543913231778537360L;

    /**
     * 最短匹配等待时长(秒)
     */
    @NotNull(message = "最短匹配等待时长不能为空")
    @ApiModelProperty(value = "最短匹配等待时长",
            required = true, position = 1, example = "10")
    private Integer mixWaitTime;

    /**
     * 匹配文案
     */
    @NotEmpty(message = "匹配文案不能为空")
    @ApiModelProperty(value = "匹配文案",
            required = true, position = 2, example = "正在匹配...")
    private String matchingText;

    /**
     * 引导文案出现时间(秒)
     */
    @NotNull(message = "引导文案出现时间不能为空")
    @ApiModelProperty(value = "引导文案出现时间",
            required = true, position = 3, example = "60")
    private Integer guideTime;

    /**
     * 引导文案
     */
    @NotEmpty(message = "引导文案不能为空")
    @ApiModelProperty(value = "引导文案",
            required = true, position = 4, example = "哎呀,免费车队都满了,去试试服务更好的付费车队吧!")
    private String guideText;


    /**
     * 匹配超时时间(秒)
     */
    @NotNull(message = "匹配超时时间不能为空")
    @ApiModelProperty(value = "匹配超时时间",
            required = true, position = 5, example = "300")
    private Integer waitTimeout;

    public FreeTeamMatchingWaitVO() {
    }

    public Integer getMixWaitTime() {
        return mixWaitTime;
    }

    public void setMixWaitTime(Integer mixWaitTime) {
        this.mixWaitTime = mixWaitTime;
    }

    public String getMatchingText() {
        return matchingText;
    }

    public void setMatchingText(String matchingText) {
        this.matchingText = matchingText;
    }

    public Integer getGuideTime() {
        return guideTime;
    }

    public void setGuideTime(Integer guideTime) {
        this.guideTime = guideTime;
    }

    public String getGuideText() {
        return guideText;
    }

    public void setGuideText(String guideText) {
        this.guideText = guideText;
    }

    public Integer getWaitTimeout() {
        return waitTimeout;
    }

    public void setWaitTimeout(Integer waitTimeout) {
        this.waitTimeout = waitTimeout;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}