package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 顶级职业列表
 *
 * @author zhangfang
 */
public class RedisTopCareer implements Serializable {

    private static final long serialVersionUID = -5392955592097909821L;
    /**
     * 顶级职业代码
     */
    private Integer careerCode;
    /**
     * 顶级职业名称
     */
    private String careerName;

    private List<RedisAwakeningCareer> awakeningCareer;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<RedisAwakeningCareer> getAwakeningCareer() {
        return awakeningCareer;
    }

    public void setAwakeningCareer(
            List<RedisAwakeningCareer> awakeningCareer) {
        this.awakeningCareer = awakeningCareer;
    }
}
