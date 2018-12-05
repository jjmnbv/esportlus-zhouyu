package com.kaihei.esportingplus.core.utils;

import java.util.Random;

/**
 * @Author liuyang
 * @Description 验证码生成器
 * @Date 2018/10/25 16:16
 **/
@FunctionalInterface
public interface VerificationCodeGenerator<T> {

    T generate(int length);

    VerificationCodeGenerator<String> RANDOM_NUM = (length) -> {
        String retStr = "";
        String strTable = "1234567890";
        int len = strTable.length();
        while (retStr.length() < length){
            int intR = (int) Math.floor(Math.random() * len);
            retStr += strTable.charAt(intR);
        }
        return retStr;
    };

}
