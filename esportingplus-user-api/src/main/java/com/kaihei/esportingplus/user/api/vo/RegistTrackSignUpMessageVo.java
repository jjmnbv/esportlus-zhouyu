package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-21 15:14
 * @Description:
 */
public class RegistTrackSignUpMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = 3708548644646835345L;

    private String uid;

    private String saDistinctId;

    public RegistTrackSignUpMessageVo() {
    }

    public RegistTrackSignUpMessageVo(String uid, String saDistinctId) {
        this.uid = uid;
        this.saDistinctId = saDistinctId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSaDistinctId() {
        return saDistinctId;
    }

    public void setSaDistinctId(String saDistinctId) {
        this.saDistinctId = saDistinctId;
    }
}
