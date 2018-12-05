package com.kaihei.esportingplus.marketing.api.event;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 16:31
 * @Description:
 */
public class ConsumeUser implements Serializable {
    private static final long serialVersionUID = -3509927227188501893L;

    private String uid;
    /**
     * 分，1个暴击币为100分，1个暴击币传100过来
     */
    private Integer coin;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}
