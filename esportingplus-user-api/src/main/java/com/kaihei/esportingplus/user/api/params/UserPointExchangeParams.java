package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * 用户鸡分兑换请求参数
 *
 * @author keriezhang
 * @date: 2018/10/9 11:45
 * @version: 1.0
 */

@Validated
public class UserPointExchangeParams implements Serializable {

    private static final long serialVersionUID = -5198224124401247399L;

    @NotNull(message = "用户uid不能为空")
    private String uid;

    @NotNull(message = "兑换鸡分值不能为空")
    private Integer exchangeAmount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(Integer exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }
}
