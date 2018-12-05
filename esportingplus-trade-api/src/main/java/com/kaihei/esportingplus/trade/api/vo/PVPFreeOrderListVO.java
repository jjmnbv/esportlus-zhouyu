package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author
 */
public class PVPFreeOrderListVO implements Serializable {

    private static final long serialVersionUID = 6497753399827066064L;
    private UserInfoVo userInfo;

    private PVPFreeOrderVo order;

    public UserInfoVo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVo userInfo) {
        this.userInfo = userInfo;
    }

    public PVPFreeOrderVo getOrder() {
        return order;
    }

    public void setOrder(PVPFreeOrderVo order) {
        this.order = order;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
