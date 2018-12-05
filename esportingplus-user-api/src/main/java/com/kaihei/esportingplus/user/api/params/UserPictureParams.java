package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;

/**
 * 修改用户信息参数
 *
 * @author linruihe
 * @date 2018/11/02
 */
public class UserPictureParams implements Serializable {

    private static final long serialVersionUID = 5902683012521464619L;

    /**
     * 图片Url
     */
    private String url;

    /**
     * 图片操作方法
     */
    private String action;

    /**
     * 图片Id
     */
    private Integer id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
