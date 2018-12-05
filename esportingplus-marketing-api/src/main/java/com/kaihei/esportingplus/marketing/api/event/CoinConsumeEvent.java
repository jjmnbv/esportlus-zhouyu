package com.kaihei.esportingplus.marketing.api.event;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-10 17:50
 * @Description:
 */
public class CoinConsumeEvent extends UserEvent implements Serializable {

    private static final long serialVersionUID = 2156973536684822880L;

    private List<ConsumeUser> users;

    private String payOrderNo;

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public List<ConsumeUser> getUsers() {
        return users;
    }

    public void setUsers(List<ConsumeUser> users) {
        this.users = users;
    }
}