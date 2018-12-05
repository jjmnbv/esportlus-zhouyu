package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

/**
 * @author admin
 */
public class CompaintItemPictureVo implements Serializable {

    private static final long serialVersionUID = 5972157094866762484L;

    /**
     * 网络图片地址
     */
    private String pictureUrl;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String toString() {
        return "CompaintItemPictureVo{" +
                "pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
