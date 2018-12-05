package com.kaihei.esportingplus.core.api.params;

import java.util.List;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/30 10:31
 */
public class QiniuQueryParam {
    /**
     * 七牛token类型
     */
    private String tokenType;
    /**
     * 游戏订单号
     */
    private String gameOrderId;
    /**
     * 图片编号
     */
    private String pictureNo;
    /**
     * 序列号
     */
    private String serialNo;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getGameOrderId() {
        return gameOrderId;
    }

    public void setGameOrderId(String gameOrderId) {
        this.gameOrderId = gameOrderId;
    }

    public String getPictureNo() {
        return pictureNo;
    }

    public void setPictureNo(String pictureNo) {
        this.pictureNo = pictureNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
