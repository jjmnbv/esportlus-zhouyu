package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 提现黑名单
 * @author chenzhenjun
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_transfer_user_id", columnNames = "userId")})
public class TransferBlackRecord implements Serializable {

    private static final long serialVersionUID = 8006583269041574124L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(10) COMMENT '用户id'")
    private String userId;

    @Column(columnDefinition = "longtext COMMENT '备注'")
    private String remark;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @PrePersist
    void onCreate() {
        this.setCreateDate(new Timestamp((new Date()).getTime()));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
