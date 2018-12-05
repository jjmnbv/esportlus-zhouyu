package com.kaihei.esportingplus.user.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @author xiekeqing
 * @Title: ConversionUtil
 * @Description: 进制转换工具类
 * @date 2018/9/1919:38
 */
public class ConversionUtil {

    private static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final long HIX = 62;
    private static final String PADCHAR = "0";

    public static String encode(long num , int length) {

        StringBuilder sb = new StringBuilder();
        int remainder = 0;

        //将num对62取模，从DIGITS匹配出对应的字符
        while (true) {
            remainder = Long.valueOf(num % HIX).intValue();
            sb.append(DIGITS.charAt(remainder));
            num = num / HIX;
            if (num == 0) {
                break;
            }
        }

        //TODO 改为使用随机数
        String value = sb.reverse().toString();
        return StringUtils.rightPad(value, length, PADCHAR);
    }

    public static void main(String[] args) {
        System.out.println(encode(61 ,8));
    }
}
