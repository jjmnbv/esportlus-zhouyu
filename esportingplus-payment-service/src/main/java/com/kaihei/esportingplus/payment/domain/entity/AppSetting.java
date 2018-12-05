package com.kaihei.esportingplus.payment.domain.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 支付应用信息表
 *
 * @author zhouyu, haycco
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name="uk_app_id", columnNames = "appId") })
public class AppSetting extends AbstractEntity {

    private static final long serialVersionUID = 2055526264046808010L;

    @Column(columnDefinition = "varchar(32) comment '已知有接入支付应用APP的ID'", nullable = false)
    private String appId;

    @Column(columnDefinition = "varchar(32) comment '支付应用APP的名称'", nullable = false)
    private String appName;

    @Column(columnDefinition = "varchar(8) default 'OPEN' comment '支付应用状态：OPEN-开启；CLOSE-关闭'")
    private String state;

    @ManyToMany(mappedBy = "appSettings", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PayChannel> payChannels = new HashSet<>();

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<PayChannel> getPayChannels() {
        return payChannels;
    }

    public void setPayChannels(Set<PayChannel> payChannels) {
        this.payChannels = payChannels;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
