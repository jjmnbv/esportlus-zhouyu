package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "members_userrelationship")
public class MembersUserrelationship {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "follow_user_id")
    private Integer followUserId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "follow_datetime")
    private Date followDatetime;

    @Column(name = "is_friend")
    private Boolean isFriend;

    public MembersUserrelationship(Integer id, Integer followUserId, Integer userId, Date followDatetime, Boolean isFriend) {
        this.id = id;
        this.followUserId = followUserId;
        this.userId = userId;
        this.followDatetime = followDatetime;
        this.isFriend = isFriend;
    }

    public MembersUserrelationship() {
        super();
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return follow_user_id
     */
    public Integer getFollowUserId() {
        return followUserId;
    }

    /**
     * @param followUserId
     */
    public void setFollowUserId(Integer followUserId) {
        this.followUserId = followUserId;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return follow_datetime
     */
    public Date getFollowDatetime() {
        return followDatetime;
    }

    /**
     * @param followDatetime
     */
    public void setFollowDatetime(Date followDatetime) {
        this.followDatetime = followDatetime;
    }

    /**
     * @return is_friend
     */
    public Boolean getIsFriend() {
        return isFriend;
    }

    /**
     * @param isFriend
     */
    public void setIsFriend(Boolean isFriend) {
        this.isFriend = isFriend;
    }
}