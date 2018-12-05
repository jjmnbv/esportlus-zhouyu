package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

public class FrontSecondaryCareer implements Serializable {

    private static final long serialVersionUID = -1127305269723284124L;

    /**
     * 职业代码
     */
    private Integer careerCode;
    /**
     * 职业名称
     */
    private String careerName;
    /**
     * 职业图标
     */
    private String careerIcon;
    public Integer getCareerCode() {
        return careerCode;
    }

    public void setCareerCode(Integer careerCode) {
        this.careerCode = careerCode;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public String getCareerIcon() {
        return careerIcon;
    }

    public void setCareerIcon(String careerIcon) {
        this.careerIcon = careerIcon;
    }
}
