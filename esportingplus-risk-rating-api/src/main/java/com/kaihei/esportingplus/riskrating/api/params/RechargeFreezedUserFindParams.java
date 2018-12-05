package com.kaihei.esportingplus.riskrating.api.params;

import io.swagger.annotations.ApiModelProperty;

public class RechargeFreezedUserFindParams {

    /**
     * 页号
     */
    @ApiModelProperty(value = "页号", position = 1, example = "1")
    private int page;

    /**
     * 页面大小
     */
    @ApiModelProperty(value = "页面大小", position = 1, example = "10")
    private int pageSize = 20;
    /**
     * 用户uid
     */
    @ApiModelProperty(value = "用户uid")
    private String uid = "";

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
