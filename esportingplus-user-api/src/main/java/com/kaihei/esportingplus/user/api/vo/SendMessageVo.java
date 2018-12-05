package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zhaozhenlin
 * @description:
 * @date: 2018/10/9 14:58
 */
public class SendMessageVo implements Serializable {
    /**
     * 发送者uid
     **/
    private String sender;
    /**
     * 接收者uid或uid列表, 如果是群消息, 这里为群id或群id列表
     **/
    private List<String> reciever;
    /**
     * 模板id
     **/
    private String templateId;
    /**
     * 模板数据
     **/
    private Map<String, Object> data;
    /**
     * 模板数据
     **/
    private Map<String, SendMessageDataVo> entity;
    /**
     * 仅用于ios app被杀掉后，推送内容
     **/
    private String pushContent;
    /**
     * uid，只用于群消息，发送给群里指定用户
     **/
    private List<String> toUsers;
    /**
     * 自己是否接收消息
     **/
    private Boolean toSelf;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReciever() {
        return reciever;
    }

    public void setReciever(List<String> reciever) {
        this.reciever = reciever;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public List<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
    }

    public Boolean getToSelf() {
        return toSelf;
    }

    public void setToSelf(Boolean toSelf) {
        this.toSelf = toSelf;
    }
}
