package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

/**
 * 次级职业，觉醒职业列表
 *
 * @author zhangfang
 */
public class RedisAwakeningCareer implements Serializable {

    private static final long serialVersionUID = 8538083390771939659L;
    /**
     * 一转职业代码
     */
    private Integer classChangeCode;
    /**
     * 一转职业名称
     */
    private String classChangeName;
    /**
     * 一觉职业代码
     */
    private Integer firstAwakeningCode;
    /**
     * 一觉职业名称
     */
    private String firstAwakeningName;
    /**
     * 二觉职业代码
     */
    private Integer secondAwakeningCode;
    /**
     * 二觉职业名称
     */
    private String secondAwakeningName;
    /**
     * 二觉职业头像地址
     */
    private String secondAwakeningIcon;

    public Integer getClassChangeCode() {
        return classChangeCode;
    }

    public void setClassChangeCode(Integer classChangeCode) {
        this.classChangeCode = classChangeCode;
    }

    public String getClassChangeName() {
        return classChangeName;
    }

    public void setClassChangeName(String classChangeName) {
        this.classChangeName = classChangeName;
    }

    public Integer getFirstAwakeningCode() {
        return firstAwakeningCode;
    }

    public void setFirstAwakeningCode(Integer firstAwakeningCode) {
        this.firstAwakeningCode = firstAwakeningCode;
    }

    public String getFirstAwakeningName() {
        return firstAwakeningName;
    }

    public void setFirstAwakeningName(String firstAwakeningName) {
        this.firstAwakeningName = firstAwakeningName;
    }

    public Integer getSecondAwakeningCode() {
        return secondAwakeningCode;
    }

    public void setSecondAwakeningCode(Integer secondAwakeningCode) {
        this.secondAwakeningCode = secondAwakeningCode;
    }

    public String getSecondAwakeningName() {
        return secondAwakeningName;
    }

    public void setSecondAwakeningName(String secondAwakeningName) {
        this.secondAwakeningName = secondAwakeningName;
    }

    public String getSecondAwakeningIcon() {
        return secondAwakeningIcon;
    }

    public void setSecondAwakeningIcon(String secondAwakeningIcon) {
        this.secondAwakeningIcon = secondAwakeningIcon;
    }
}
