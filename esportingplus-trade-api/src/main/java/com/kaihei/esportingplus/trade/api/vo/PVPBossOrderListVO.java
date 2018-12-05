package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author
 */
public class PVPBossOrderListVO implements Serializable {

    private static final long serialVersionUID = 5565332364814392405L;
    private UserInfoVo userInfo;

    private PVPBossOrderVO order;

    public UserInfoVo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVo userInfo) {
        this.userInfo = userInfo;
    }

    public PVPBossOrderVO getOrder() {
        return order;
    }

    public void setOrder(PVPBossOrderVO order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
