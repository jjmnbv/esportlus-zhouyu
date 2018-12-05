package com.kaihei.esportingplus.payment.util;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.ApplePayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * ios 苹果支付工具类
 *
 * @author xusisi
 * @create 2018-08-20 22:08
 **/
@Component
public class IosVerifyUtils {

    private static Logger logger = LoggerFactory.getLogger(IosVerifyUtils.class);

    @Value("${app.apple.pay.url.sandbox}")
    private static String sandboxURL;

    @Value("${app.apple.pay.url.verify}")
    private static String verifyURL;

    /**
     * 苹果服务器验证
     *
     * @param receipt 账单
     * @return 返回结果 沙盒 https://sandbox.itunes.apple.com/verifyReceipt
     * @url 要验证的地址
     */
    public static String buyAppVerify(String receipt, int type) throws ApplePayException {
        //环境判断 线上/开发环境用不同的请求链接
        String url = "";
        if (type == 0) {
            //沙盒测试
            url = sandboxURL;
        } else {
            //线上测试
            url = verifyURL;
        }

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //设置超时时间为15分钟
            conn.setConnectTimeout(1000 * 60 * 15);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());
            Map<String, String> param = new HashMap<>();
            param.put("receipt-data", receipt);
            String jsonStr = JSONObject.toJSONString(param);
            System.out.println("receipt-data >> " + jsonStr);
            //拼成固定的格式传给平台
            String str = String.format(Locale.CHINA, jsonStr);
            logger.info("str >> " + str);
            hurlBufOus.write(str.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception ex) {
            logger.error("苹果服务器校验异常：{}", ex.getMessage());
            throw new ApplePayException(BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_SERVER_EXCEPTION);
        }

    }

    ///**
    // * 用BASE64加密
    // */
    //public static String getBASE64(String str) {
    //    byte[] b = str.getBytes();
    //    String s = null;
    //    if (b != null) {
    //        s = new sun.misc.BASE64Encoder().encode(b);
    //    }
    //    return s;
    //}

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
