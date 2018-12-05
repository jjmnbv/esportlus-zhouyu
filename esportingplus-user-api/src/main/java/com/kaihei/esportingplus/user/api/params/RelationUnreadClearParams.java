package com.kaihei.esportingplus.user.api.params;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/3 21:20
 **/
public class RelationUnreadClearParams {

    private String uid;

    /**1:粉丝  2：好友*/
    private Integer type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
