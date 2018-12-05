package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

/**
 * @author zhangfang
 */
public class RedisGame implements Serializable {

    private static final long serialVersionUID = -3010788243254107818L;
    /** 游戏代码 **/
    private Integer code;
    /** 游戏名称**/
    private String name;
    /** 游戏图标 **/
    private String icon;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
