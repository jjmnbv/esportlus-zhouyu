package com.kaihei.esportingplus.payment.util;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.MD5Util;
import com.kaihei.esportingplus.payment.api.PayConstants;
import com.kaihei.esportingplus.payment.domain.entity.TenpaySetting;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jolokia.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

/**
 * @Author: zhouyu
 * @Date: 2018/10/16 15:08
 * @Description:支付签名工具类，包括支付宝和微信
 */
public class PaySignUtils {

    private static final Logger logger = LoggerFactory.getLogger(PaySignUtils.class);

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     *
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     */
    public static Map<String, String> processResponseXml(String xmlStr, TenpaySetting tenpaySetting) throws BusinessException {

        Map<String, String> respData = PayXmlutils.xmlTomap(xmlStr);
        if (!respData.containsKey(TenpayConstants.RETURN_CODE)) {
            // 微信/QQ支付-返回数据错误
            logger.error(BizExceptionEnum.EXTERNAL_TENPAY_RESPONSE_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_RESPONSE_ERROR);
        }

        String returnCode = respData.get(TenpayConstants.RETURN_CODE);
        if (returnCode.equals(TenpayConstants.RETURN_CODE_FAIL)) {
            return respData;
        } else if (returnCode.equals(TenpayConstants.RETURN_CODE_SUCCESS)) {
            if (isSignatureValid(respData, tenpaySetting.getApiSecret(), tenpaySetting.getSignType())) {
                return respData;
            } else {
                // 微信/QQ支付-签名无效
                logger.error("xmlStr : {} ", xmlStr);
                logger.error(BizExceptionEnum.EXTERNAL_TENPAY_SIGN_INVALIDATION.getErrMsg());
                throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_SIGN_INVALIDATION);

            }
        } else {
            // 微信/QQ支付-returnCode值无效
            logger.error("returnCode : {} ", returnCode);
            logger.error(BizExceptionEnum.EXTERNAL_TENPAY_RETURN_CODE_INVALIDATION.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_RETURN_CODE_INVALIDATION);
        }

    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data     Map类型数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, String signType)
            throws BusinessException {
        if (!data.containsKey(TenpayConstants.SIGN)) {
            return false;
        }
        String sign = data.get(TenpayConstants.SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data     待签名数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, String signType) throws BusinessException {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        if ("MD5".equals(signType)) {

            try {
                return MD5(sb.toString()).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(BizExceptionEnum.EXTERNAL_TENPAY_MD5_ERROR.getErrMsg());
                throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_MD5_ERROR);
            }

        } else if ("HMACSHA256".equals(signType)) {

            try {
                return HMACSHA256(sb.toString(), key);
            } catch (Exception e) {
                // 生成 HMACSHA256 加密异常
                logger.error(BizExceptionEnum.EXTERNAL_TENPAY_HMACSHA256_ERROR.getErrMsg());
                throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_HMACSHA256_ERROR);
            }

        } else {
            // 微信支付无效的签名算法
            logger.error("signType : {} ", signType);
            logger.error(BizExceptionEnum.EXTERNAL_TENPAY_SIGNATURE_INVALIDATION.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_SIGNATURE_INVALIDATION);
        }
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 拼接参数Md5加密
     */
    public static String sign(Map<String, String> data, String key) {
        //获取签名字段
        String content = getSignContent(data, true);
        if (StringUtils.isBlank(content)) {
            return null;
        }
        StringBuilder buf = new StringBuilder(content);
        buf.append("&").append("key=" + key);
        String sign = MD5Util.MD5Encode(buf.toString(), "utf-8");
        return sign;
    }

    /**
     * AES解密
     *
     * @param reqinfo 报文base64解密
     * @param apikey  商户秘钥
     * @return 解密后的xml报文
     */
    public static String decryptData(String reqinfo, String apikey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                MD5Util.MD5Encode(apikey, "UTF-8").toLowerCase().getBytes(), ALGORITHM);
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(Base64Util.decode(reqinfo)));
    }

    /**
     * @param withSignType sign_type字段，  支付宝支付回调会有此字段。 true,计算签名(微信)；  false 不计算签名(支付宝)
     */
    public static String getSignContent(Map<String, String> data, boolean withSignType) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        TreeMap<String, String> map = new TreeMap<String, String>(data);

        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String k = entry.getKey();
            if (StringUtils.isBlank(k)) {
                continue;
            }
            if ("class".equalsIgnoreCase(k) || "key".equalsIgnoreCase(k) || "sign"
                    .equalsIgnoreCase(k)) {
                continue;
            }
            if (!withSignType && "sign_type".equalsIgnoreCase(k)) {
                continue;
            }
            String v = entry.getValue();
            // 字段为空，不参与签名
            if (StringUtils.isBlank(v)) {
                continue;
            }
            buf.append(k);
            buf.append("=");
            buf.append(v);
            buf.append("&");
        }

        buf = buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /**
     * 获取沙箱环境签名，沙箱环境的支付需要沙箱独立的apikey
     */
    public static String getSandBoxSign(TenpaySetting tenpaySetting) throws BusinessException {
        Map<String, String> param = new HashMap<String, String>();
        //需要真实商户号
        param.put("mch_id", tenpaySetting.getMchId());
        //随机字符
        param.put("nonce_str", RandomStringUtils.random(32, true, true));
        //通过SDK生成签名其中API_KEY为商户对应的真实密钥
        String sign = null;
        try {
            sign = generateSignature(param, tenpaySetting.getApiSecret(), "MD5");
        } catch (Exception e) {
            // 微信获取沙箱秘钥，生成签名失败
            e.printStackTrace();
            logger.error(BizExceptionEnum.EXTERNAL_TENPAY_SANDBOX_SIGNATURE_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_SANDBOX_SIGNATURE_FAIL);
        }
        param.put("sign", sign);
        //将map转换为xml格式
        String xml = PayXmlutils.mapToXml(param);
        //沙箱密钥获取api
        String urlSufix = PayConstants.SANDBOX_SIGN_KEY_URL;
        String reslut = null;
        try {
            reslut = new TencentPayRquest(tenpaySetting).requestWithoutCert(urlSufix, xml);
        } catch (Exception e) {
            // 微信获取沙箱秘钥，请求失败
            e.printStackTrace();
            logger.error(BizExceptionEnum.EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_FAIL);
        }
        logger.info("微信获取沙箱秘钥，响应：{}", reslut);
        Map<String, String> param1 = new HashMap<String, String>();
        try {
            param1 = PayXmlutils.xmlTomap(reslut);
        } catch (Exception e) {
            // 微信获取沙箱签名响应解析失败
            e.printStackTrace();
            logger.error(BizExceptionEnum.EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_RESPONSE_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_RESPONSE_FAIL);
        }
        String key = param1.get("sandbox_signkey");
        return key;
    }

//    public static boolean checkMd5Sign(String content, String sign, String key) {
//        if (StringUtils.isBlank(content)) {
//            return false;
//        }
//        StringBuilder buf = new StringBuilder(content);
//        buf.append("&key=" + key);
//
//        String uf = MD5Util.MD5Encode(buf.toString(), "utf-8");
//    }

    /**
     * 如果是map：
     * SignUtils.checkRSASign(SignUtils.getSignContent(map, false), sign, publicKey)
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
//    public static boolean checkRSASign(String content, String sign, String publicKey) {
//        if (StringUtils.isBlank(content)) {
//            return false;
//        }
//        return RSA.verify(sign, content, publicKey);
//    }
}
