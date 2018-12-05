package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 可供接入支付渠道表
 *
 * @author zhouyu, haycco
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name="uk_tag", columnNames = "tag") })
public class PayChannel extends AbstractEntity {

    private static final long serialVersionUID = 747054469007007117L;

    @Column(columnDefinition = "varchar(32) default '' comment '支付渠道名称'")
    private String name;

    @Column(columnDefinition = "varchar(8) comment '支付渠道标签'", nullable = false)
    private String tag;

    @Column(columnDefinition = "varchar(8) default 'ENABLE' comment '渠道状态：ENABLE-可用；DISABLE-禁用'")
    private String state;

    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.REMOVE)
    @JoinTable(name = "app_setting_pay_channel",
            joinColumns = @JoinColumn(name = "payChannelId"), inverseJoinColumns = @JoinColumn(name = "appSettingId"),
            foreignKey = @ForeignKey(name="fk_pay_channel_id"), inverseForeignKey = @ForeignKey(name="fk_app_setting_id"))
    @JsonSerialize(using = SimpleAppSettingsSerializer.class)
    private Set<AppSetting> appSettings = new HashSet<AppSetting>();

    public PayChannel() {
    }

    public PayChannel(Long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<AppSetting> getAppSettings() {
        return appSettings;
    }

    public void setAppSettings(Set<AppSetting> appSettings) {
        this.appSettings = appSettings;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
