package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/29 11:36
 **/
@Table(name = "user_fans")
public class UserFans {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    @Column(name = "fans_id")
    private String fansId;

    @Column(name = "create_time")
    private Date createTime;

    public UserFans() {
    }

    public UserFans(Integer id, String uid, String fansId, Date createTime) {
        this.id = id;
        this.uid = uid;
        this.fansId = fansId;
        this.createTime = createTime;
    }

    public UserFans(String uid, String fansId, Date createTime) {
        this.uid = uid;
        this.fansId = fansId;
        if (createTime != null){
            this.createTime = createTime;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
