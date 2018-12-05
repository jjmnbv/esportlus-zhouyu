package com.kaihei.esportingplus.api.vo.freeteam;

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
 * 免费车队可加入次数配置参数
 * -- 这个配置在 2018/11/15被废弃了(产品说不需要了)...
 * -- 这个配置在 2018/11/19被加回来了!(产品说保留)...
 *
 * @author liangyi
 */

@Validated
@ApiModel(description = "免费车队可加入次数配置")
@Builder
@AllArgsConstructor
public class FreeTeamJoinTimesVO implements Serializable {

    private static final long serialVersionUID = 9151748036642503065L;

    /**
     * 单个用户最大累计上免费上车次数,-1表示不限制
     */
    @NotNull
    @Range(min = -1, message = "-1表示不限制最大累计上免费上车次数")
    @ApiModelProperty(value = "单个用户最大累计上免费上车次数",
            required = true, position = 1, example = "-1")
    private Integer maxJoinTimes;

    /**
     * 单个用户最多可拥有免费机会数,-1表示不限制
     */
    @NotNull
    @Range(min = -1, message = "-1表示不限制最大机会数")
    @ApiModelProperty(value = "单个用户最多可拥有免费机会数",
            required = true, position = 2, example = "-1")
    private Integer maxChance;


    public FreeTeamJoinTimesVO() {
    }

    public Integer getMaxJoinTimes() {
        return maxJoinTimes;
    }

    public void setMaxJoinTimes(Integer maxJoinTimes) {
        this.maxJoinTimes = maxJoinTimes;
    }

    public Integer getMaxChance() {
        return maxChance;
    }

    public void setMaxChance(Integer maxChance) {
        this.maxChance = maxChance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
