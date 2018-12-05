package com.kaihei.esportingplus.payment.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统扣减费用比率设置表
 *
 * @author zhouyu,haycco
 */
@Table
@Entity
public class DeductRatioSetting extends AbstractEntity {

    private static final long serialVersionUID = -6315479899779799967L;
    /**
     * 适用费率标记：CALC_ORDER 订单结算；WITHDRAW 收益提现
     */
    @Column(columnDefinition = "varchar(64) default '' comment '适用费率标记：CALC_ORDER 订单结算；WITHDRAW 收益提现'")
    private String tag;
    /**
     * 描述信息
     */
    @Column(columnDefinition = "varchar(128) default '' comment '订单结算抽成比率、收益提现抽成税率'")
    private String description;
    /**
     * 抽成比例
     */
    @Column(columnDefinition = "float(4,2) default 0.00 comment '抽成比例'")
    private Float ratio;
    /**
     * 是否生效
     */
    @Column(columnDefinition = "varchar(8) default 'ENABLE' comment '状态: ENABLE-有效、DISABLE-禁用'")
    private String state;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
