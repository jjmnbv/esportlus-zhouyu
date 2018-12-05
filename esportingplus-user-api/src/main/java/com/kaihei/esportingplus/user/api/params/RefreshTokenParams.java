package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;

/**
 * 刷新token参数
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 20:05
 */
public class RefreshTokenParams implements Serializable {

    private static final long serialVersionUID = 8634451813997546664L;

    private String pythonToken;
    private String version;

    public RefreshTokenParams() {
    }

    public RefreshTokenParams(String pythonToken, String version) {
        this.pythonToken = pythonToken;
        this.version = version;
    }

    public String getPythonToken() {
        return pythonToken;
    }

    public void setPythonToken(String pythonToken) {
        this.pythonToken = pythonToken;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
