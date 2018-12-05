package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/30 20:00
 **/
@Table(name = "user_friend")
public class UserFriend {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    @Column(name = "friend_id")
    private String friendId;

    @Column(name = "create_time")
    private Date createTime;

    public UserFriend() {
    }

    public UserFriend(Integer id, String uid, String friendId, Date createTime) {
        this.id = id;
        this.uid = uid;
        this.friendId = friendId;
        this.createTime = createTime;
    }

    public UserFriend(String one, String other, Date createTime) {
        int i = one.compareToIgnoreCase(other);
        if (i > 0) {
            this.uid = other;
            this.friendId = one;
        } else {
            this.uid = one;
            this.friendId = other;
        }

        if (createTime != null) {
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

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
