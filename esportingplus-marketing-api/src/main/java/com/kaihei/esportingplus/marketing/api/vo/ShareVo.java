package com.kaihei.esportingplus.marketing.api.vo;

/**
 * 分享
 */
public class ShareVo {
    /**
     * 分享跳转地址
     */
    private String url;

    /**
     * 分享图标
     */
    private String icon;

    /**
     * 分享内容
     */
    private String content;

    /**
     * 分享标题
     */
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
