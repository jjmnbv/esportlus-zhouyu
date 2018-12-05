package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author admin
 */
public class GameType implements Serializable {

    private static final long serialVersionUID = -3311573477339595911L;

    /**
     * 游戏code
     */
    private int code;

    /**
     * 游戏名称
     */
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
