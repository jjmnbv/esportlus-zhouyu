package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-21 15:04
 * @Description:
 */
public class RegistingMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = 3189765731730488142L;

    private String nickname;

    private Integer sex;

    private Date registerTime;

    private String registWay;

    private String chickenId;

    private String phone;

    private String channel;

    private String version;

    private String uid;

    public RegistingMessageVo() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegistWay() {
        return registWay;
    }

    public void setRegistWay(String registWay) {
        this.registWay = registWay;
    }

    public String getChickenId() {
        return chickenId;
    }

    public void setChickenId(String chickenId) {
        this.chickenId = chickenId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
