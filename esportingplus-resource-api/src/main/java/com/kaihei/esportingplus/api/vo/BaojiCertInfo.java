package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡认证查询返回结果 -- 调用 python 端接口使用
 * @author liangyi
 */
public class BaojiCertInfo implements Serializable {

    private static final long serialVersionUID = 3939681086707597285L;

    /**
     * 认证的游戏 code
     */
    private Integer gameCode;

    /**
     * 暴鸡等级 100/101/102/300
     */
    private Integer baojiLevel;

    public BaojiCertInfo() {
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}