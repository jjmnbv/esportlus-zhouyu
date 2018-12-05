package com.kaihei.esportingplus.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 读取RSA相关配置数据
 *
 * @author haycco
 */
@ConfigurationProperties(prefix = "spring.encrypt")
@Configuration
public class EncryptConfig {

    private String privateKey;  //RSA加密私钥
    private String charset = "UTF-8";
    private boolean debug = false;  // 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
