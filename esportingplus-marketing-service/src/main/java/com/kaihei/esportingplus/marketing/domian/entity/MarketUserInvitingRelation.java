package com.kaihei.esportingplus.marketing.domian.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "market_user_inviting_relation")
public class MarketUserInvitingRelation {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 用户userid
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 邀请人userid
     */
    @Column(name = "inviting_userid")
    private Integer invitingUserid;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    public MarketUserInvitingRelation(Long id, Integer userId, Integer invitingUserid, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.invitingUserid = invitingUserid;
        this.createTime = createTime;
    }

    public MarketUserInvitingRelation() {
        super();
    }

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户userid
     *
     * @return user_id - 用户userid
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户userid
     *
     * @param userId 用户userid
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取邀请人userid
     *
     * @return inviting_userid - 邀请人userid
     */
    public Integer getInvitingUserid() {
        return invitingUserid;
    }

    /**
     * 设置邀请人userid
     *
     * @param invitingUserid 邀请人userid
     */
    public void setInvitingUserid(Integer invitingUserid) {
        this.invitingUserid = invitingUserid;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}