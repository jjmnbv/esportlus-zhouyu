package com.kaihei.esportingplus.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 车队常规配置
 * @author liangyi
 */
@Validated
@ApiModel(description = "车队常规配置")
public class TeamGeneralConfigVO implements Serializable {

    private static final long serialVersionUID = -4335824245522968448L;

    /**
     * 游戏位置数配置
     */
    @NotEmpty
    @ApiModelProperty(value = "游戏位置数不能为空",
            required = true, position = 1, example = "[]")
    private List<GamePositionVO> position;


    public TeamGeneralConfigVO() {
    }

    public List<GamePositionVO> getPosition() {
        return position;
    }

    public void setPosition(List<GamePositionVO> position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}