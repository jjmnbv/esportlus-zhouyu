package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * IM防骚扰及虚拟机注册登陆实体类
 * @author chenzhenjun
 */
@Entity
public class ImMachineConfig extends AbstractEntity {

    /**
     * 当日累计聊天触达用户数
     */
    @Column(nullable = false, columnDefinition = "int(4) COMMENT '当日累计聊天触达用户数'")
    private Integer chatCount;

    /**
     * 当日累计发布同一文本条数
     */
    @Column(nullable = false, columnDefinition = "int(4) COMMENT '当日累计发布同一文本条数'")
    private Integer contentCount;

    /**
     * 当日累计发布总文本条数
     */
    @Column(nullable = false, columnDefinition = "int(10) COMMENT '当日累计发布总文本条数'")
    private Integer totalContentCount;

    /**
     * 暴鸡暴娘过滤配置(0-不过滤,1-过滤)
     */
    @Column(nullable = false, columnDefinition = "int(1) COMMENT '暴鸡暴娘过滤配置(0-不过滤,1-过滤)'")
    private Integer baojiChosen;

    /**
     * 普通用户过滤配置(0-不过滤,1-过滤)
     */
    @Column(nullable = false, columnDefinition = "int(1) COMMENT '普通用户过滤配置(0-不过滤,1-过滤)'")
    private Integer userChosen;

    /**
     * 虚拟机判断开关(0-关闭，1-开启)
     */
    @Column(nullable = false, columnDefinition = "int(1) COMMENT '虚拟机判断开关(0-关闭，1-开启)'")
    private Integer machineSwitch;

    /**
     * 数美风险分识别配置
     */
    @Column(nullable = false, columnDefinition = "int(4) COMMENT '数美风险分识别配置'")
    private Integer machineScore;

    /**
     * 数美设备登陆数限制
     */
    @Column(nullable = false, columnDefinition = "int(4) COMMENT '数美设备登陆数限制'")
    private Integer loginLimit;

    /**
     * 数美设备注册数限制
     */
    @Column(nullable = false, columnDefinition = "int(4) COMMENT '数美设备登陆数限制'")
    private Integer registerLimit;

    public Integer getChatCount() {
        return chatCount;
    }

    public void setChatCount(Integer chatCount) {
        this.chatCount = chatCount;
    }

    public Integer getContentCount() {
        return contentCount;
    }

    public void setContentCount(Integer contentCount) {
        this.contentCount = contentCount;
    }

    public Integer getTotalContentCount() {
        return totalContentCount;
    }

    public void setTotalContentCount(Integer totalContentCount) {
        this.totalContentCount = totalContentCount;
    }

    public Integer getBaojiChosen() {
        return baojiChosen;
    }

    public void setBaojiChosen(Integer baojiChosen) {
        this.baojiChosen = baojiChosen;
    }

    public Integer getUserChosen() {
        return userChosen;
    }

    public void setUserChosen(Integer userChosen) {
        this.userChosen = userChosen;
    }

    public Integer getMachineSwitch() {
        return machineSwitch;
    }

    public void setMachineSwitch(Integer machineSwitch) {
        this.machineSwitch = machineSwitch;
    }

    public Integer getMachineScore() {
        return machineScore;
    }

    public void setMachineScore(Integer machineScore) {
        this.machineScore = machineScore;
    }

    public Integer getLoginLimit() {
        return loginLimit;
    }

    public void setLoginLimit(Integer loginLimit) {
        this.loginLimit = loginLimit;
    }

    public Integer getRegisterLimit() {
        return registerLimit;
    }

    public void setRegisterLimit(Integer registerLimit) {
        this.registerLimit = registerLimit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
