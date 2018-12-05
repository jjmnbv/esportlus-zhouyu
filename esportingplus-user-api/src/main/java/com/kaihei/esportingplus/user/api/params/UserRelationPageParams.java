package com.kaihei.esportingplus.user.api.params;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/3 19:51
 **/
public class UserRelationPageParams {

    private Integer page;
    private Integer size;

    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
