package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @author zhaozhenlin
 * @description: 发送消息模板内容
 * @date: 2018/10/9 16:29
 */
public class SendMessageDataVo implements Serializable {

    private static final long serialVersionUID = -3905853077297000318L;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String desc;

    public SendMessageDataVo(){}

    public SendMessageDataVo(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
