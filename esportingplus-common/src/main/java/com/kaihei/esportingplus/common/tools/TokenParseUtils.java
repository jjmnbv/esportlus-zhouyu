package com.kaihei.esportingplus.common.tools;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * token 解析工具类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/11/15 18:01
 */
public class TokenParseUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(TokenParseUtils.class);

    private final static String PYTHON_TOKEN_SEPARATOR = ".";
    private final static String PYTHON_TOKEN_REGEX = "\\.";

    public static String parseToken(String pythonToken) {
        Pair<String, String> pythonTokenPair = parsePythonToken(pythonToken);
        return pythonTokenPair == null ? null : pythonTokenPair.getRight();
    }

    public static Pair<String, String> parsePythonToken(String pythonToken) {
        try {
            if (StringUtils.isNotBlank(pythonToken) && pythonToken
                    .contains(PYTHON_TOKEN_SEPARATOR)) {
                String[] tokenInfos = pythonToken.split(PYTHON_TOKEN_REGEX);
                return MutablePair.of(tokenInfos[0], tokenInfos[1]);
            }
        } catch (Exception e) {
            LOGGER.error("token 解析失败，此token非法:{}", pythonToken);
        }

        return null;
    }

}
