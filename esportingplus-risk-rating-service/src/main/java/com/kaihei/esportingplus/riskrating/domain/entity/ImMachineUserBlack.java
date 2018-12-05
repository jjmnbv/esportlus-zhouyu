package com.kaihei.esportingplus.riskrating.domain.entity;


import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;
/**
 * IM防骚扰及虚拟机判断需求——用户黑名单表
 * @author chenzhenjun
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_user_id", columnNames = "userId")})
public class ImMachineUserBlack extends AbstractEntity{

    /**
     * 用户id
     */
    @Column(nullable = false, columnDefinition = "varchar(8) COMMENT '用户id'")
    private String userId;

    /**
     * 最后一次登陆时间
     */
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '最后一次登陆时间'")
    private Date lastLoginTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
