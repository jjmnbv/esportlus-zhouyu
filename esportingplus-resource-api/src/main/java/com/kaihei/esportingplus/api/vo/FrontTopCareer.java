package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.List;

public class FrontTopCareer implements Serializable {

    private static final long serialVersionUID = 2279898908189991803L;

    /**
     * 顶级职业代码
     */
    private Integer careerCode;
    /**
     * 顶级职业名称
     */
    private String careerName;

    private List<FrontSecondaryCareer> children;

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

    public List<FrontSecondaryCareer> getChildren() {
        return children;
    }

    public void setChildren(List<FrontSecondaryCareer> children) {
        this.children = children;
    }
}
