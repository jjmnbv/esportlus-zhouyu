package com.kaihei.esportingplus.riskrating.api.params;


import com.kaihei.esportingplus.common.data.Castable;

public class UserRechargeFreezeParams implements Castable {

    private String uid;

    private String msg;

    private Long operatorId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
