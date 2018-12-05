package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询投诉与反馈列表返回Vo
 *
 * @author yangshidong
 * @date 2018/12/3
 * */
public class FeedbackVo implements Serializable {

    private static final long serialVersionUID = -8144920112470975243L;

    /**
     * id（数据库记录id）
     * */
    private int id;

    /**
     * 反馈id(年份+日期+0001递增)
     * */
    private String feedbackId;

    /**
     * 反馈人( UID/鸡牌号/昵称)
     * */
    private String feedbackUserId;

    /**
     * 反馈内容
     * */
    private String content;

    /**
     * 反馈图片地址(多地址用英文逗号隔开)
     * */
    private String url;

    /**
     * 反馈时间
     * */
    private Date createDatetime;

    /**
     * 处理状态(0:待处理 1:已处理)
     * */
    private short handleStatus;

    /**
     * 联系电话
     * */
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackUserId() {
        return feedbackUserId;
    }

    public void setFeedbackUserId(String feedbackUserId) {
        this.feedbackUserId = feedbackUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public short getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(short handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
