package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "baoji_celebrity")
public class BaoJiCelebrity {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * UID
     */
    private String uid;

    /**
     * 暴鸡标签
     */
    @Column(name = "baoji_tag")
    private Short baojiTag;

    /**
     * 个人说明
     */
    private String desc;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 录音地址
     */
    private String sound;

    /**
     * 是否置顶
     */
    @Column(name = "is_sticky")
    private Short isSticky;

    /**
     * 置顶时间
     */
    @Column(name = "sticky_time")
    private Date stickyTime;

    /**
     * 审核状态
     */
    @Column(name = "verify_status")
    private Short verifyStatus;

    /**
     * 审核时间
     */
    @Column(name = "verify_time")
    private Date verifyTime;

    /**
     * 审核用户userId
     */
    @Column(name = "verify_user_id")
    private Integer verifyUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public BaoJiCelebrity(Integer id, String username, String uid, Short baojiTag, String desc, String cover, String sound, Short isSticky, Date stickyTime, Short verifyStatus, Date verifyTime, Integer verifyUserId, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.uid = uid;
        this.baojiTag = baojiTag;
        this.desc = desc;
        this.cover = cover;
        this.sound = sound;
        this.isSticky = isSticky;
        this.stickyTime = stickyTime;
        this.verifyStatus = verifyStatus;
        this.verifyTime = verifyTime;
        this.verifyUserId = verifyUserId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public BaoJiCelebrity() {
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
     * 获取用户昵称
     *
     * @return username - 用户昵称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户昵称
     *
     * @param username 用户昵称
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取UID
     *
     * @return uid - UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置UID
     *
     * @param uid UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取暴鸡标签
     *
     * @return baoji_tag - 暴鸡标签
     */
    public Short getBaojiTag() {
        return baojiTag;
    }

    /**
     * 设置暴鸡标签
     *
     * @param baojiTag 暴鸡标签
     */
    public void setBaojiTag(Short baojiTag) {
        this.baojiTag = baojiTag;
    }

    /**
     * 获取个人说明
     *
     * @return desc - 个人说明
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置个人说明
     *
     * @param desc 个人说明
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * 获取封面地址
     *
     * @return cover - 封面地址
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置封面地址
     *
     * @param cover 封面地址
     */
    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    /**
     * 获取录音地址
     *
     * @return sound - 录音地址
     */
    public String getSound() {
        return sound;
    }

    /**
     * 设置录音地址
     *
     * @param sound 录音地址
     */
    public void setSound(String sound) {
        this.sound = sound == null ? null : sound.trim();
    }

    /**
     * 获取是否置顶
     *
     * @return is_sticky - 是否置顶
     */
    public Short getIsSticky() {
        return isSticky;
    }

    /**
     * 设置是否置顶
     *
     * @param isSticky 是否置顶
     */
    public void setIsSticky(Short isSticky) {
        this.isSticky = isSticky;
    }

    /**
     * 获取置顶时间
     *
     * @return sticky_time - 置顶时间
     */
    public Date getStickyTime() {
        return stickyTime;
    }

    /**
     * 设置置顶时间
     *
     * @param stickyTime 置顶时间
     */
    public void setStickyTime(Date stickyTime) {
        this.stickyTime = stickyTime;
    }

    /**
     * 获取审核状态
     *
     * @return verify_status - 审核状态
     */
    public Short getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * 设置审核状态
     *
     * @param verifyStatus 审核状态
     */
    public void setVerifyStatus(Short verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    /**
     * 获取审核时间
     *
     * @return verify_time - 审核时间
     */
    public Date getVerifyTime() {
        return verifyTime;
    }

    /**
     * 设置审核时间
     *
     * @param verifyTime 审核时间
     */
    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    /**
     * 获取审核用户userId
     *
     * @return verify_user_id - 审核用户userId
     */
    public Integer getVerifyUserId() {
        return verifyUserId;
    }

    /**
     * 设置审核用户userId
     *
     * @param verifyUserId 审核用户userId
     */
    public void setVerifyUserId(Integer verifyUserId) {
        this.verifyUserId = verifyUserId;
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

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}