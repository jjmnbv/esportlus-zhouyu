package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-25 16:02
 * @Description:
 */
public class ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = -5616035006205531265L;

    private String uniqueNo;

    public String getUniqueNo() {
        return uniqueNo;
    }

    public void setUniqueNo(String uniqueNo) {
        this.uniqueNo = uniqueNo;
    }
}
