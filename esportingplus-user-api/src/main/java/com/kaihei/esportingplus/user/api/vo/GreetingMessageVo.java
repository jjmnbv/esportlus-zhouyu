package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-21 14:56
 * @Description: 消息消费，发送问候语短信对象
 */
public class GreetingMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = 7461202958693077234L;


    private List<String> uidList = new ArrayList<>();

    public GreetingMessageVo() {
    }

    public GreetingMessageVo(List<String> uidList) {
        this.uidList = uidList;
    }

    public GreetingMessageVo(String uid) {
        if (this.uidList == null) {
            this.uidList = new ArrayList<>();
        }
        this.uidList.add(uid);
    }

    public List<String> getUidList() {
        return uidList;
    }

    public void setUidList(List<String> uidList) {
        this.uidList = uidList;
    }
}
