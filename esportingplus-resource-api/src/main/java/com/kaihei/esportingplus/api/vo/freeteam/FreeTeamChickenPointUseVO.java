package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 *@Description: 免费车队使用鸡分配置Modle
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/10/8 15:21
*/
public class FreeTeamChickenPointUseVO implements Serializable {

    private static final long serialVersionUID = -3486107511075432839L;
    /**
     * 免费车队使用鸡分配置-主键ID
     */
    private Integer id;

    /**
     * 积分兑换类型：1=兑换暴击值 2=兑换滴滴接单资格 3=兑换开车资格 4=兑换推荐位
     */
    private Integer exchangeType;

    /**
     * 是否支持使用鸡分: 0=否 1=是
     */
    private Integer useChikenpoint;

    /**
     * 兑换比例：使用鸡分
     */
    private Integer exchangeRateChikenpoint;

    /**
     * 兑换比例：可兑换暴击值
     */
    private String exchangeRateBaojiValue;

    /**
     * 兑换开始时间
     */
    private Date exchangeStarttime;

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

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(Integer exchangeType) {
        this.exchangeType = exchangeType;
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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}