package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * 查询uid对应的unionId信息
 *
 * @author yangshidong
 * @date 2018/11/8
 */
public class UserBindWxUnionIdVo implements Serializable {
    private static final long serialVersionUID = -550510330362195873L;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 已绑定微信的unionid
     */
    private String unionid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
