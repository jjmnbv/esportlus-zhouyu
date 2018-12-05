package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/24 16:22
 **/
@Table(name = "user_follow")
public class UserFollow {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    @Column(name = "follow_id")
    private String followId;

    @Column(name = "create_time")
    private Date createTime;

    public UserFollow() {
    }

    public UserFollow(String uid, String followId) {
        this.uid = uid;
        this.followId = followId;
    }

    public UserFollow(String uid, String followId, Date createTime) {
        this.uid = uid;
        this.followId = followId;
        if (createTime != null){
            this.createTime = createTime;
        }
    }

    public UserFollow(Integer id, String uid, String followId, Date createTime) {
        this.id = id;
        this.uid = uid;
        this.followId = followId;
        this.createTime = createTime;
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

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
