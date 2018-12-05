package com.kaihei.esportingplus.payment.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AES128 算法 CBC 模式 PKCS7Padding 填充模式
 *
 * 其中CBC模式需要添加一个参数iv--对称解密算法初始向量 iv 要实现用PKCS7Padding填充，需要用到bouncycastle组件来实现
 * 
 * @author haycco
 */
public class AESUtils {

    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    // 算法名称
    public static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    // 默认对称解密算法初始向量 iv
    private static byte[] iv = { 0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };

    /**
     * 加密方法 --使用默认iv时
     *
     * @param content 要加密的字符串
     * @param keyBytes 加密密钥
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = encryptOfDiyIV(content, keyBytes, iv);
        return encryptedText;
    }

    /**
     * 解密方法 --使用默认iv时
     * 
     * @param encryptedData 要解密的字符串
     * @param keyBytes 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes) {
        byte[] encryptedText = decryptOfDiyIV(encryptedData, keyBytes, iv);
        return encryptedText;
    }

    /**
     * 加密方法 ---自定义对称解密算法初始向量 iv
     * 
     * @param content 要加密的字符串
     * @param keyBytes 加密密钥
     * @param ivs 自定义对称解密算法初始向量 iv
     * @return 加密的结果
     */
    public static byte[] encryptOfDiyIV(byte[] content, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        logger.debug("加密向量IV：" + new String(Base64Utils.encode(ivs)));
        try {
            // 如果密钥不足16位，那么就补足
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            // 转化成JDK加密的密钥格式
            Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivs));
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bos.write(cipher.update(content));
            bos.write(cipher.doFinal(content));
            encryptedText = bos.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage());
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("加密异常：" + e.getMessage());
        }
        return encryptedText;
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes 解密密钥
     * @param ivs 自定义对称解密算法初始向量 iv
     * @return
     */
    public static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        logger.debug("解密向量IV：" + new String(Base64Utils.encode(ivs)));
        try {
            // 如果密钥不足16位，那么就补足
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            // 转化成JDK加密的密钥格式
            Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
          
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bos.write(cipher.update(encryptedData));
            bos.write(cipher.doFinal(encryptedData));
            encryptedText = bos.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage());
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("解密异常：" + e.getMessage());
        }
        return encryptedText;
    }

    public static void main(String[] args) {
        // 密文
        String encryptedData = "sMWQyLPYHvDwhqORbFhCuyuCTbs6OybG+7QYcdKxcwX4u6ENqkkekFPliXBJKnD4";

//        String iv = "r7BXXKkLb8qrSNn05n0qiA==";
//        //String appid = "wx4f4bc4dec97d474b";
        String sessionKey = "YmFja2VuZF9hZG1pbg==";
        byte[] sessionKeyByte = Base64Utils.decodeFromString(sessionKey);
        byte[] encryptedDataByte = Base64Utils.decodeFromString(encryptedData);
//        byte[] ivByte = Base64Utils.decodeFromString(iv);
//        // 解密
        logger.info("密文内容：" + encryptedData);
        byte[] dec = AESUtils.decrypt(encryptedDataByte, sessionKeyByte);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String result = mapper.writeValueAsString(new String(dec));
            logger.info("解密后的内容：" + result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        logger.info("加密后的密钥：" + new String(Base64Utils.encode("backend_admin".getBytes())));

        // 加密
        String content = "加密前的文本字符串内容";
        logger.info("加密前内容：" + content);
        byte[] encryptedData2 = AESUtils.encrypt(content.getBytes(), sessionKeyByte);
        String result = new String(Base64Utils.encode(encryptedData2));
        logger.info("加密后的内容：" + result);
    }

}
