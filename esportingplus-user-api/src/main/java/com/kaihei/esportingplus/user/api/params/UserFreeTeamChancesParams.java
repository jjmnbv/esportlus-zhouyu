package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description:免费车队次数操作参数
 * @date: 2018/11/16 16:00
 */
public class UserFreeTeamChancesParams implements Serializable {

    private static final long serialVersionUID = -412377617927756915L;

    private String uid;

    private Integer freeCount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Integer freeCount) {
        this.freeCount = freeCount;
    }
}
