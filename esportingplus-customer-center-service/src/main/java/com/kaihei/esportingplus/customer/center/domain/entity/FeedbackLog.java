package com.kaihei.esportingplus.customer.center.domain.entity;

import lombok.Builder;

import java.util.Date;
import javax.persistence.*;

@Builder
@Table(name = "feedback_log")
public class FeedbackLog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 反馈人id信息(UID/鸡牌号/昵称)
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 前端页面显示反馈id
     */
    @Column(name = "display_id")
    private String displayId;

    /**
     * 反馈图片地址
     */
    private String url;

    /**
     * 处理状态
     */
    @Column(name = "handle_status")
    private Short handleStatus;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 创建时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;

    /**
     * 回复人id
     */
    @Column(name = "reply_user_id")
    private Integer replyUserId;

    /**
     * 回复时间
     */
    @Column(name = "reply_datetime")
    private Date replyDatetime;

    /**
     * 反馈内容
     */
    private String content;

    public FeedbackLog(Integer id, String userId, String displayId, String url, Short handleStatus, String phone, Date createDatetime, Integer replyUserId, Date replyDatetime, String content) {
        this.id = id;
        this.userId = userId;
        this.displayId = displayId;
        this.url = url;
        this.handleStatus = handleStatus;
        this.phone = phone;
        this.createDatetime = createDatetime;
        this.replyUserId = replyUserId;
        this.replyDatetime = replyDatetime;
        this.content = content;
    }

    public FeedbackLog() {
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
     * 获取反馈人id信息(UID/鸡牌号/昵称)
     *
     * @return user_id - 反馈人id信息(UID/鸡牌号/昵称)
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置反馈人id信息(UID/鸡牌号/昵称)
     *
     * @param userId 反馈人id信息(UID/鸡牌号/昵称)
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取前端页面显示反馈id
     *
     * @return display_id - 前端页面显示反馈id
     */
    public String getDisplayId() {
        return displayId;
    }

    /**
     * 设置前端页面显示反馈id
     *
     * @param displayId 前端页面显示反馈id
     */
    public void setDisplayId(String displayId) {
        this.displayId = displayId == null ? null : displayId.trim();
    }

    /**
     * 获取反馈图片地址
     *
     * @return url - 反馈图片地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置反馈图片地址
     *
     * @param url 反馈图片地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取处理状态
     *
     * @return handle_status - 处理状态
     */
    public Short getHandleStatus() {
        return handleStatus;
    }

    /**
     * 设置处理状态
     *
     * @param handleStatus 处理状态
     */
    public void setHandleStatus(Short handleStatus) {
        this.handleStatus = handleStatus;
    }

    /**
     * 获取联系电话
     *
     * @return phone - 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_datetime - 创建时间
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * 设置创建时间
     *
     * @param createDatetime 创建时间
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * 获取回复人id
     *
     * @return reply_user_id - 回复人id
     */
    public Integer getReplyUserId() {
        return replyUserId;
    }

    /**
     * 设置回复人id
     *
     * @param replyUserId 回复人id
     */
    public void setReplyUserId(Integer replyUserId) {
        this.replyUserId = replyUserId;
    }

    /**
     * 获取回复时间
     *
     * @return reply_datetime - 回复时间
     */
    public Date getReplyDatetime() {
        return replyDatetime;
    }

    /**
     * 设置回复时间
     *
     * @param replyDatetime 回复时间
     */
    public void setReplyDatetime(Date replyDatetime) {
        this.replyDatetime = replyDatetime;
    }

    /**
     * 获取反馈内容
     *
     * @return content - 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置反馈内容
     *
     * @param content 反馈内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}