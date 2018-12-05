package com.kaihei.esportingplus.marketing.api.vo;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description:用户免单累计信息
 * @date: 2018/11/15 17:20
 */
public class UserFreeCouponsInfoVo implements Serializable {

    private static final long serialVersionUID = -5817689547773396976L;

    /**
     * 累计免单次数
     */
    private Integer usedCount;

    /**
     * 可用免单次数
     */
    private Integer availableCount;

    /**
     * 文案
     */
    private String text;

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
