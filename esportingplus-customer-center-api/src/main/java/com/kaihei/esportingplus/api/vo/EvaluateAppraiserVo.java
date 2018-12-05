package com.kaihei.esportingplus.api.vo;

import lombok.Builder;

import java.io.Serializable;

/**
 * 评价者信息
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@Builder
public class EvaluateAppraiserVo implements Serializable {

    private static final long serialVersionUID = -3845921671519994127L;

    /**
     * 评价者头像地址
     */
    private String thumbnail;

    /**
     * 评价者用户名
     */
    private String username;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
