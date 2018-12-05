package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡订单列表
 * @author zhangfang
 */
public class PVPBaojiOrderListVO implements Serializable {

    private static final long serialVersionUID = 450760941389627068L;
    private UserInfoVo userInfo;

    private PVPBaojiOrderVO order;

    public UserInfoVo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVo userInfo) {
        this.userInfo = userInfo;
    }

    public PVPBaojiOrderVO getOrder() {
        return order;
    }

    public void setOrder(PVPBaojiOrderVO order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
