package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-15 17:08
 * @Description:
 */
public class PythonFreeteamChancesInfoVo implements Serializable {

    private static final long serialVersionUID = -3846634901436959330L;

    /**
     * 累计免单次数
     */
    private String usedCount;

    /**
     * 	可用免单次数
     */
    private String availableCount;

    public String getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(String usedCount) {
        this.usedCount = usedCount;
    }

    public String getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(String availableCount) {
        this.availableCount = availableCount;
    }
}
