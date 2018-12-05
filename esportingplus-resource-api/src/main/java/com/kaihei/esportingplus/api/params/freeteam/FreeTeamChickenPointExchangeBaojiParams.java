package com.kaihei.esportingplus.api.params.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

/**
 * @author Orochi-Yzh
 * @Description: 免费车队-使用鸡分配置-兑换暴击值Modle
 * @dateTime 2018/10/8 15:21
 */
@Validated
@ApiModel(description = "免费车队-使用鸡分配置-兑换暴击值")
public class FreeTeamChickenPointExchangeBaojiParams implements Serializable {


    private static final long serialVersionUID = -6920936931674291623L;
    @ApiModelProperty(value = "免费车队使用鸡分配置-兑换暴击值-主键ID", required = false, position = 1, example = "1")
    private Integer id;

    @ApiModelProperty(value = "是否支持使用鸡分: 0=否 1=是", required = true, position = 2, example = "1")
    @Range(min = 0,max = 1,message = "是否支持使用鸡分参数错误：0=否 1=是")
    private Integer useChikenpoint;

    /**
     * 兑换比例：使用的鸡分数
     */
    @ApiModelProperty(value = "兑换比例-使用的鸡分数", required = true, position = 3, example = "1")
    private Integer exchangeRateChikenpoint;

    /**
     * 兑换比例：可兑换暴击值
     */
    @ApiModelProperty(value = "兑换比例-可兑换暴击值", required = true, position = 4, example = "1")
    private String exchangeRateBaojiValue;

    @ApiModelProperty(value = "兑换比例-可兑换暴击值", required = true, position = 5, example = "1")
    private Integer exchangeStartWeekId;

    /**
     * 兑换开始周的值(来源dictionary)
     */
    private String exchangeStartWeekValue;

    /**
     * 兑换开始时间
     */
    @ApiModelProperty(value = "兑换开始时间", required = true, position = 6, example = "1")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2}",message = "兑换开始时间格式错误：要求 HH:mm:ss")
    private Date exchangeStarttime;

    /**
     * 兑换结束周id(来源dictionary)
     */
    private Integer exchangeEndWeekId;

    /**
     * 兑换结束周的值(来源dictionary)
     */
    private String exchangeEndWeekValue;

    /**
     * 兑换结束时间
     */
    private Date exchangeEndtime;

    /**
     * 单次可兑换的最低鸡分单位
     */
    private Integer singleLowestPoint;

    /**
     * 单次可兑换的最高鸡分单位
     */
    private Integer singleHighestPoint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUseChikenpoint() {
        return useChikenpoint;
    }

    public void setUseChikenpoint(Integer useChikenpoint) {
        this.useChikenpoint = useChikenpoint;
    }

    public Integer getExchangeRateChikenpoint() {
        return exchangeRateChikenpoint;
    }

    public void setExchangeRateChikenpoint(Integer exchangeRateChikenpoint) {
        this.exchangeRateChikenpoint = exchangeRateChikenpoint;
    }

    public String getExchangeRateBaojiValue() {
        return exchangeRateBaojiValue;
    }

    public void setExchangeRateBaojiValue(String exchangeRateBaojiValue) {
        this.exchangeRateBaojiValue = exchangeRateBaojiValue;
    }

    public Date getExchangeStarttime() {
        return exchangeStarttime;
    }

    public void setExchangeStarttime(Date exchangeStarttime) {
        this.exchangeStarttime = exchangeStarttime;
    }

    public Date getExchangeEndtime() {
        return exchangeEndtime;
    }

    public void setExchangeEndtime(Date exchangeEndtime) {
        this.exchangeEndtime = exchangeEndtime;
    }

    public Integer getSingleLowestPoint() {
        return singleLowestPoint;
    }

    public void setSingleLowestPoint(Integer singleLowestPoint) {
        this.singleLowestPoint = singleLowestPoint;
    }

    public Integer getSingleHighestPoint() {
        return singleHighestPoint;
    }

    public void setSingleHighestPoint(Integer singleHighestPoint) {
        this.singleHighestPoint = singleHighestPoint;
    }

    public Integer getExchangeStartWeekId() {
        return exchangeStartWeekId;
    }

    public void setExchangeStartWeekId(Integer exchangeStartWeekId) {
        this.exchangeStartWeekId = exchangeStartWeekId;
    }

    public String getExchangeStartWeekValue() {
        return exchangeStartWeekValue;
    }

    public void setExchangeStartWeekValue(String exchangeStartWeekValue) {
        this.exchangeStartWeekValue = exchangeStartWeekValue;
    }

    public Integer getExchangeEndWeekId() {
        return exchangeEndWeekId;
    }

    public void setExchangeEndWeekId(Integer exchangeEndWeekId) {
        this.exchangeEndWeekId = exchangeEndWeekId;
    }

    public String getExchangeEndWeekValue() {
        return exchangeEndWeekValue;
    }

    public void setExchangeEndWeekValue(String exchangeEndWeekValue) {
        this.exchangeEndWeekValue = exchangeEndWeekValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}