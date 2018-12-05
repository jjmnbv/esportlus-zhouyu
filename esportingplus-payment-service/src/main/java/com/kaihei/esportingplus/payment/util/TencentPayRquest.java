package com.kaihei.esportingplus.payment.util;

import com.kaihei.esportingplus.payment.api.PayConstants;
import com.kaihei.esportingplus.payment.domain.entity.TenpaySetting;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * @Author: zhouyu
 * @Date: 2018/10/26 14:35
 * @Description: 腾讯支付处理
 */
public class TencentPayRquest {

    private static final Logger logger = LoggerFactory.getLogger(TencentPayRquest.class);

    public static final String WXPAYSDK_VERSION = "WXPaySDK/3.0.9";
    public static final String USER_AGENT = WXPAYSDK_VERSION +
            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " "
            + System.getProperty("os.version") +
            ") Java/" + System.getProperty("java.version") + " HttpClient/" + HttpClient.class
            .getPackage().getImplementationVersion();

    TenpaySetting tenCentpaySetting;

    public TencentPayRquest(TenpaySetting tenpaySetting) {
        this.tenCentpaySetting = tenpaySetting;
    }

    /**
     * 请求，只请求一次，不做重试
     *
     * @param useCert 是否使用证书，针对退款、撤销等操作
     */
    private String requestOnce(String url, String data, boolean useCert) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 使用证书
            char[] password = tenCentpaySetting.getMchId()
                    .toCharArray();
            InputStream certStream = tenCentpaySetting.getApiCaertificatePathStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        } else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();
        //支付URl拼接
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(PayConstants.READ_TIME_OUT)
                .setConnectTimeout(PayConstants.CONNECT_TIME_OUT).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent",
                USER_AGENT + " " + tenCentpaySetting.getMchId());
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }

    private String request(String urlSuffix, String data, boolean useCert) throws Exception {
        Exception exception = null;
        //超时时间
        long elapsedTimeMillis = 0;
        try {
            long start = System.currentTimeMillis();
            String result = requestOnce(urlSuffix, data, useCert);
            long end = System.currentTimeMillis();
            elapsedTimeMillis = end - start;
            return result;
        } catch (UnknownHostException ex) {
            logger.error("微信支付域名解析错误");
            exception = ex;
        } catch (ConnectTimeoutException ex) {
            exception = ex;
            logger.error("微信支付域名解析错误网络超时:time【{}】", elapsedTimeMillis);
        } catch (SocketTimeoutException ex) {
            exception = ex;
            logger.error("微信支付网络链接错误");
        } catch (Exception ex) {
            logger.error("微信支付其他作物错误");
            exception = ex;

        }
        throw exception;
    }

    /**
     * 可重试的，非双向认证的请求
     */
  /*  public String requestWithoutCert(String urlSuffix, String data)
            throws Exception {
        return this.request(urlSuffix, data, config.getHttpConnectTimeoutMs(),
                config.getHttpReadTimeoutMs(), false, autoReport);
    }*/

    /**
     * 非双向认证的请求
     */
    public String requestWithoutCert(String urlSuffix, String data) throws Exception {
        return this.request(urlSuffix, data, false);
    }

    /*  *//**
     * 双向认证的请求
     *//*
    public String requestWithCert(String urlSuffix, String uuid, String data, boolean autoReport)
            throws Exception {
        return this.request(urlSuffix, uuid, data, config.getHttpConnectTimeoutMs(),
                config.getHttpReadTimeoutMs(), true, autoReport);
    }*/

    /**
     * 可重试的，双向认证的请求
     */
    public String requestWithCert(String urlSuffix, String data) throws Exception {
        return this.request(urlSuffix, data, true);
    }

}
