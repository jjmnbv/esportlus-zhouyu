package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description: 免费车队首页状态次数Vo
 * @date: 2018/11/15 16:43
 */
public class UserFreeTeamChancesVo implements Serializable {

    private static final long serialVersionUID = -2115494120326362212L;

    /**
     * 剩余上车次数
     */
    private Integer count;
    /**
     * 1: 正常 2: 没有免费次数 3: 有进行中的
     */
    private Integer status;
    /**
     * 文案
     */
    private String text;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
