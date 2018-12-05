package com.kaihei.esportingplus.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 暴鸡接单范围
 * @author liangyi
 */
@Validated
@ApiModel(description = "暴鸡接单范围")
public class BaojiDanRangeParams implements Serializable {

    private static final long serialVersionUID = 5497157433814109984L;

    /**
     * 暴鸡接单范围 id
     */
    @ApiModelProperty(value = "暴鸡接单范围id",
            required = false, position = 1, example = "1")
    private Integer baojiDanRangeId;

    /**
     * 暴鸡等级 {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     */
    @NotNull(message = "暴鸡等级不能为空")
    @ApiModelProperty(value = "暴鸡等级",
            required = true, position = 2, example = "11")
    private Integer baojiLevel;

    /**
     * 段位下限id,来自数据字典游戏段位
     */
    @NotNull(message = "段位下限id不能为空")
    @ApiModelProperty(value = "段位下限id",
            required = true, position = 3, example = "21")
    private Integer lowerDanId;

    /**
     * 段位上限id,来自数据字典游戏段位
     */
    @NotNull(message = "段位上限id不能为空")
    @ApiModelProperty(value = "段位上限id",
            required = true, position = 4, example = "31")
    private Integer upperDanId;


    public BaojiDanRangeParams() {
        super();
    }

    public Integer getBaojiDanRangeId() {
        return baojiDanRangeId;
    }

    public void setBaojiDanRangeId(Integer baojiDanRangeId) {
        this.baojiDanRangeId = baojiDanRangeId;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public Integer getLowerDanId() {
        return lowerDanId;
    }

    public void setLowerDanId(Integer lowerDanId) {
        this.lowerDanId = lowerDanId;
    }

    public Integer getUpperDanId() {
        return upperDanId;
    }

    public void setUpperDanId(Integer upperDanId) {
        this.upperDanId = upperDanId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}