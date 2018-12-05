package com.kaihei.esportingplus.core.api.vo;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description:七牛token
 * @date: 2018/10/30 10:32
 */
public class QiniuTokenVo implements Serializable {
    private static final long serialVersionUID = -5352642518345300041L;

    /**
     * token
     */
    private String qiniuToken;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 域名
     */
    private String domain;

    public QiniuTokenVo() {
    }

    public QiniuTokenVo(String qiniuToken, String fileName, String domain) {
        this.qiniuToken = qiniuToken;
        this.fileName = fileName;
        this.domain = domain;
    }

    public String getQiniuToken() {
        return qiniuToken;
    }

    public void setQiniuToken(String qiniuToken) {
        this.qiniuToken = qiniuToken;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
