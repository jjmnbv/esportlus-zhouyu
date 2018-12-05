package com.kaihei.esportingplus.riskrating.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImMachineConfigVo implements Serializable {

    private static final long serialVersionUID = -3329908113140166677L;
    private Long id;

    private Integer chatCount;

    private Integer contentCount;

    private Integer totalContentCount;

    private Integer baojiChosen;

    private Integer userChosen;

    private Integer machineSwitch;

    private Integer machineScore;

    private Integer loginLimit;

    private Integer registerLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
