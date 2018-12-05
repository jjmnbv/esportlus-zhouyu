package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.common.tools.JacksonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

/**
 * 免费车队配置参数
 * @author liangyi
 */
@Validated
@ApiModel(description = "免费车队配置")
@Builder
@AllArgsConstructor
public class FreeTeamConfigVO implements Serializable {

    private static final long serialVersionUID = -2316148612289692274L;

    /**
     * 暴鸡/暴娘可上免费车队开关,1:开启,0:关闭
     */
    @NotNull
    @Range(min = 0, max = 1, message = "暴鸡开关取值为0或1")
    @ApiModelProperty(value = "暴鸡可创建免费车队开关",
            required = true, position = 2, example = "1")
    private Integer baojiSwitch;

    /**
     * 白名单可创建免费车队开关,1:开启,0:关闭
     */
    @NotNull
    @Range(min = 0, max = 1, message = "白名单开关取值为0或1")
    @ApiModelProperty(value = "白名单用户可加入免费车队开关",
            required = true, position = 3, example = "1")
    private Integer whiteListSwitch;

    /**
     * 免费车队匹配等待
     */
    @NotNull(message = "免费车队匹配等待不能为空")
    private FreeTeamMatchingWaitVO matchingWait;

    /**
     * 免费车队可加入次数
     */
    @NotNull(message = "免费车队可加入次数不能为空")
    private FreeTeamJoinTimesVO joinTimes;

    /**
     * 免费车队首页弹窗
     */
    @NotNull(message = "免费车队首次更新弹窗不能为空")
    private FreeTeamFirstUpdateVO firstUpdate;

    public FreeTeamConfigVO() {
    }

    public Integer getBaojiSwitch() {
        return baojiSwitch;
    }

    public void setBaojiSwitch(Integer baojiSwitch) {
        this.baojiSwitch = baojiSwitch;
    }

    public Integer getWhiteListSwitch() {
        return whiteListSwitch;
    }

    public void setWhiteListSwitch(Integer whiteListSwitch) {
        this.whiteListSwitch = whiteListSwitch;
    }

    public FreeTeamMatchingWaitVO getMatchingWait() {
        return matchingWait;
    }

    public void setMatchingWait(
            FreeTeamMatchingWaitVO matchingWait) {
        this.matchingWait = matchingWait;
    }

    public FreeTeamJoinTimesVO getJoinTimes() {
        return joinTimes;
    }

    public void setJoinTimes(FreeTeamJoinTimesVO joinTimes) {
        this.joinTimes = joinTimes;
    }

    public FreeTeamFirstUpdateVO getFirstUpdate() {
        return firstUpdate;
    }

    public void setFirstUpdate(FreeTeamFirstUpdateVO firstUpdate) {
        this.firstUpdate = firstUpdate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static void main(String[] args) {
        FreeTeamConfigVO freeTeamConfigVO = FreeTeamConfigVO.builder()
                .baojiSwitch(0)
                .whiteListSwitch(1)
                .matchingWait(FreeTeamMatchingWaitVO.builder()
                        .mixWaitTime(10)
                        .matchingText("正在匹配中...")
                        .guideTime(60)
                        .guideText("去试试付费车队吧")
                        .waitTimeout(300).build())
                .joinTimes(FreeTeamJoinTimesVO.builder()
                        .maxChance(-1).maxJoinTimes(-1).build())
                .firstUpdate(FreeTeamFirstUpdateVO.builder()
                        .alertSwitch(1).alertUrl("https://www.baidu.com").build())
                .build();
        System.out.println(JacksonUtils.toJsonWithSnake(freeTeamConfigVO));
    }

}