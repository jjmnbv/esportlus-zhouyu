package com.kaihei.esportingplus.payment.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.*;

/**
 * 腾讯支付配置
 *
 * @author zhouyu
 */
@Table(indexes = {@Index(name = "idx_channel_id", columnList = "channelId")})
@Entity
@Data
public class TenpaySetting extends AbstractEntity {

    @Column(columnDefinition = "varchar(64) default '' comment '应用id'", nullable = false)
    private String appId;

    @Column(columnDefinition = "varchar(256) default '' comment '支付回调地址'", nullable = false)
    private String notifyUrl;

    @Column(columnDefinition = "varchar(64) default '' comment '商户id'", nullable = false)
    private String mchId;

    @Column(columnDefinition = "varchar(128) default '' comment 'api秘钥'", nullable = false)
    private String apiSecret;

    @Column(columnDefinition = "varchar(16) default 'MD5' comment '签名方法'", nullable = false)
    private String signType;

    @Column(columnDefinition = "varchar(64) default '' comment '微信支付证书位置cret.12'", nullable = false)
    private String apiCaertificatePath;

    @Column(columnDefinition = "varchar(64) default '' comment '渠道id'", nullable = false)
    private long channelId;

    /***
     * 是否开启沙盒环境
     * true 开启
     * false 禁用
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) default 'true' COMMENT '是否开启沙盒环境(true 开启,false 禁用)'")
    private String sandboxEnable;


    /**
     * 获得证书文件
     */
    public InputStream getApiCaertificatePathStream() {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            File file = new File(apiCaertificatePath);
            InputStream certStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            certStream.read(bytes);
            certStream.close();
            byteArrayInputStream = new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayInputStream;
    }
}
