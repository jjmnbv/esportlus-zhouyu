package com.kaihei.esportingplus.payment.config;

import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: esportingplus
 * @description: 云账户加密工具类
 * @author: xusisi
 * @create: 2018-10-24 18:22
 **/
public class CloudAccountConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudAccountConfig.class);

    /**
     * 提现到银行卡时的URL
     */
    private static String bank_card_url = "/api/payment/v1/order-realtime";


    /***
     * 提现到支付宝时的URL
     */
    private static String alipay_url = "/api/payment/v1/order-alipay";

    /****
     * 提现到微信时的URL
     */
    private static String wechat_url = "/api/payment/v1/order-wxpay";

    /***
     * 加密密钥
     */
    private static String desc_key = "rDveA900MF0Ti4mXc49a8eFT";

    /***
     * 签名
     */
    private static String app_key = "07mT2jP9gTttMqs3fk40V0Z7dkw5w529";

    /**
     * 字符编码格式
     */
    public static final String CHARSET_NAME = "utf-8";

    /**
     * 加密格式
     */
    public static final String SIGN_TYPE = "sha256";

    public static byte[] tripleDesEncrypt(byte[] content, byte[] key) throws Exception {
        byte[] icv = new byte[8];
        System.arraycopy(key, 0, icv, 0, 8);
        return tripleDesEncrypt(content, key, icv);
    }

    protected static byte[] tripleDesEncrypt(byte[] content, byte[] key, byte[] icv) throws
            Exception {
        final SecretKey secretKey = new SecretKeySpec(key, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        final IvParameterSpec iv = new IvParameterSpec(icv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(content);
    }

    public static byte[] tripleDesDecrypt(byte[] content, byte[] key) throws Exception {
        byte[] icv = new byte[8];
        System.arraycopy(key, 0, icv, 0, 8);
        return tripleDesDecrypt(content, key, icv);
    }

    protected static byte[] tripleDesDecrypt(byte[] content, byte[] key, byte[] icv) throws
            Exception {
        final SecretKey secretKey = new SecretKeySpec(key, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        final IvParameterSpec iv = new IvParameterSpec(icv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        return cipher.doFinal(content);
    }

    /**
     * 得到加密后的发送数据
     * @param data
     * @param desckey
     * @param appKey
     * @return
     * @throws Exception
     */
    public static Map<String,String> encryptData(String data, String desckey, String appKey) throws Exception {
        Map<String,String> map = new LinkedHashMap<>();
        byte[] content = data.getBytes(CHARSET_NAME);
        byte[] keyArray = desckey.getBytes(CHARSET_NAME);
        byte[] enc = tripleDesEncrypt(content, keyArray);
        byte[] enc64 = Base64.encodeBase64(enc);

        String dataStr = new String(enc64);
        map.put("data", dataStr);
        logger.info(" 云账户加密过后的数据为 ： data={}", dataStr);

        //获取秒数
        String second = String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        // 获取随机数
        String mess = "123" + second;

        String signPair = "data=%s&mess=%s&timestamp=%s&key=%s";
        String signOriginal = String.format(signPair, dataStr, mess, second, appKey);
        logger.info(" 云账户加密过后的凭证原始数据为 ： signOriginal={}", signOriginal);
        String sign = sha256_HMAC(signOriginal, appKey);
        logger.info(" 云账户加密过后的凭证为 ： sign={}", sign);
        map.put("mess", mess);
        map.put("timestamp", second);
        map.put("sign", sign);
        map.put("sign_type", SIGN_TYPE);
        return map;
    }

    /**
     * 解密数据
     * @param data
     * @param descKey
     * @return
     * @throws Exception
     */
    public static String decryptData(String data, String descKey) throws Exception {
        byte[] keyArray = descKey.getBytes(CHARSET_NAME);
        byte[] content = data.getBytes(CHARSET_NAME);
        byte[] dec64 = Base64.decodeBase64(content);
        byte[] dec = CloudAccountConfig.tripleDesDecrypt(dec64, keyArray);
        String decryptData = new String(dec);
        logger.info(" 云账户提现，解密之后数据为：decrypt: {}", decryptData);
        return decryptData;
    }

    public static void main(String[] args) throws Exception {

        String signTest = "5dfbc851e031dcabeb301e4c21e56da7752517ce76ec088bfcb3a91741f78231";
        String signStr = URLEncoder.encode(signTest, CHARSET_NAME);
        logger.info("signStr = {}", signStr);

        Map<String,String> map = new LinkedHashMap<>();
//        map.put("order_id", "180309155950h52bxrKS7pgcfseOGi0q");
//        map.put("dealer_id", "27271912");
//        map.put("broker_id", "27532644");
//        map.put("real_name", "谌珍君");
//        map.put("id_card", "43072519910722831X");
//        map.put("card_no", "15813829560");
//        map.put("pay", "0.11");
//        map.put("notes", "开黑提现");
//        map.put("check_name", "NoCheck");
//        map.put("notify_url", "http://192.168.2.54:80/v1/yunpay/notify/payment");
        map.put("dealer_id", "27271912");

        String testJson = FastJsonUtils.toJson(map);
        logger.debug("testJson >> {} ", testJson);
        byte[] content = testJson.getBytes("utf-8");
        byte[] key = desc_key.getBytes("utf-8");
        byte[] enc = CloudAccountConfig.tripleDesEncrypt(content, key);
        byte[] enc64 = Base64.encodeBase64(enc);
        System.out.println("encrypt: " + new String(enc64));
        byte[] dec64 = Base64.decodeBase64(enc64);
        byte[] dec = CloudAccountConfig.tripleDesDecrypt(dec64, key);
        System.out.println("decrypt: " + new String(dec));

        String dataStr = new String(enc64);
        String signPair = "data=%s&mess=%s&timestamp=%s&key=%s";
        String sign = String.format(signPair, dataStr, "1231541767435","1541767435",app_key);
        sha256_HMAC(sign, app_key);

    }

    /**
     * @Description: 将加密后的字节数组转换成字符串
     * @Param: [b]
     * @return: java.lang.String
     * @Author: xusisi
     * @Date: 2018/10/25
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * @Description: sha256_HMAC加密
     * @Param: [message, secret]
     * @return: java.lang.String
     * @Author: xusisi
     * @Date: 2018/10/25
     */
    public static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
            System.out.println(hash);
//            logger.debug("hash >> {} ", hash);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("hmacSHA256 error ：{} ", e.getMessage());
        }
        return hash;

    }

}
