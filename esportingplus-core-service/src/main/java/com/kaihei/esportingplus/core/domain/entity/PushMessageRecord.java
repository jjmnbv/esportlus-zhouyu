package com.kaihei.esportingplus.core.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @program: esportingplus
 * @description: 推送消息记录
 * @author: xusisi
 * @create: 2018-12-03 10:53
 **/
@Table(name = "push_message_record")
public class PushMessageRecord {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /***
     * 标签组
     */
    @Column(name = "tags", nullable = false, length = 1024, columnDefinition = "VARCHAR(1024) COMMENT '标签组' ")
    private String tags;

    /**
     * 目标操作系统，iOS、Android 最少传递一个
     */
    @Column(name = "platforms", nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '目标操作系统，iOS、Android 最少传递一个' ")
    private String platforms;

    /***
     * 发送次数：-1表示无限次
     */
    @Column(name = "send_times", nullable = false, length = 4, columnDefinition = "int(4) COMMENT '发送次数：-1表示无限次' ")
    private Integer sendTimes;

    /***
     * 发送渠道（多选必填）：1、站内消息，2、站外push
     */
    @Column(name = "send_channels", nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '发送渠道（多选必填）：1站内消息，2站外push' ")
    private String sendChannels;

    /***
     * 后续动作（单选必填）：
     * 打开APP首页、打开APP内部页面、打开内部H5页面，打开第三方网页
     */
    @Column(name = "follow_action", nullable = false, length = 2, columnDefinition = "tinyint(4) COMMENT '后续动作 : 1打开APP首页;2打开APP内部页面;3打开内部H5页面;" +
            "4打开第三方网页' ")
    private Integer followAction;

    /***
     * 跳转地址
     * 若选择打开app内部页面、内部h5页面或第三方网页，则“跳转地址”项必填
     */
    @Column(name = "url", length = 255, columnDefinition = "VARCHAR(255) COMMENT '跳转地址' ")
    private String url;

    /***
     * 推送方式（单选必填）:立即推送
     */
    @Column(name = "push_mode", nullable = false, length = 2, columnDefinition = "tinyint(4) COMMENT '推送方式（单选必填）:1立即推送' ")
    private Integer pushMode;

    /**
     * 推送类型：单选必填 应用消息
     */
    @Column(name = "push_type", nullable = false, length = 2, columnDefinition = "tinyint(4) COMMENT '推送类型：单选必填 1、应用消息' ")
    private Integer pushType;

    /***
     *推送形式：图文推送(RichContentMessage)，纯文字推送(TextMessage)
     */
    @Column(name = "push_form", nullable = false, length = 2, columnDefinition = "tinyint(4) COMMENT '推送形式：1、RichContentMessage(图文推送)，2、TextMessage" +
            "(纯文字推送)' ")
    private Integer pushForm;

    /***
     * 推送标题
     */
    @Column(name = "title", length = 255, columnDefinition = "VARCHAR(255) COMMENT '推送标题' ")
    private String title;

    /***
     * 消息中图片地址
     */
    @Column(name = "image_uri", length = 255, columnDefinition = "VARCHAR(255) COMMENT '消息中图片地址' ")
    private String imageUri;

    /***
     * 推送内容
     */
    @Column(name = "content", length = 2048, columnDefinition = "varchar(2048) COMMENT '推送内容' ")
    private String content;

    /***
     * push消息发送数量
     */
    @Column(name = "send_number", columnDefinition = "int(10) COMMENT 'push消息发送数量'")
    private Integer sendNumber;

    /***
     * push消息成功触达数量
     */
    @Column(name = "success_number", columnDefinition = "int(10) COMMENT 'push消息成功触达数量'")
    private Integer successNumber;

    /***
     * push消息点击数量
     */
    @Column(name = "click_number", columnDefinition = "int(10) COMMENT 'push消息点击数'")
    private Integer clickNumber;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create", nullable = false, columnDefinition = "timestamp  COMMENT '创建时间' ")
    private Date gmtCreate;

    /**
     * 数据操作员
     */
    @Column(name = "operator", nullable = false, columnDefinition = "varchar(64) COMMENT '数据操作员' ")
    private String operator;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified", columnDefinition = "timestamp  COMMENT '修改时间' ")
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public Integer getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(Integer sendTimes) {
        this.sendTimes = sendTimes;
    }

    public String getSendChannels() {
        return sendChannels;
    }

    public void setSendChannels(String sendChannels) {
        this.sendChannels = sendChannels;
    }

    public Integer getFollowAction() {
        return followAction;
    }

    public void setFollowAction(Integer followAction) {
        this.followAction = followAction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPushMode() {
        return pushMode;
    }

    public void setPushMode(Integer pushMode) {
        this.pushMode = pushMode;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public Integer getPushForm() {
        return pushForm;
    }

    public void setPushForm(Integer pushForm) {
        this.pushForm = pushForm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(Integer sendNumber) {
        this.sendNumber = sendNumber;
    }

    public Integer getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(Integer successNumber) {
        this.successNumber = successNumber;
    }

    public Integer getClickNumber() {
        return clickNumber;
    }

    public void setClickNumber(Integer clickNumber) {
        this.clickNumber = clickNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
