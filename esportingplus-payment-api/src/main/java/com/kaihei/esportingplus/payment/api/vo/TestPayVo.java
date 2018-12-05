package com.kaihei.esportingplus.payment.api.vo;

import java.io.Serializable;

/**
 * @program: esportingplus
 * @description: 测试用的
 * @author: xusisi
 * @create: 2018-11-07 16:04
 **/
public class TestPayVo implements Serializable {

    String orderString;

    public String getOrderString() {
        return orderString;
    }

    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }

    @Override
    public String toString() {
        return "TestPayVo{" +
                "orderString='" + orderString + '\'' +
                '}';
    }
}
