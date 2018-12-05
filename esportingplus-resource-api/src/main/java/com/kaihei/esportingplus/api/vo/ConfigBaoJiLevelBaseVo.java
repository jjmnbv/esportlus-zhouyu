package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

@Validated
public class ConfigBaoJiLevelBaseVo implements Serializable {

    private static final long serialVersionUID = 5605936126890270861L;
    /**
     * 暴鸡等级
     */
    private Integer baojiLevel;

    /**
     * 暴鸡等级系统
     */
    private String baojiLevelRate;


    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public String getBaojiLevelRate() {
        return baojiLevelRate;
    }

    public void setBaojiLevelRate(String baojiLevelRate) {
        this.baojiLevelRate = baojiLevelRate;
    }
}
