package com.kaihei.esportingplus.common.tools;

import static com.kaihei.esportingplus.common.enums.BizExceptionEnum.DECODE_TOKEN_FAIL;
import static com.kaihei.esportingplus.common.enums.BizExceptionEnum.INVALID_BASIC_TOKEN;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

/**
 * 描述
 *
 * @author Orochi-Yzh
 * @dateTime 2018/4/27 17:01
 * @updatetor
 */
public class AuthUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUtils.class);
    private static final String BASIC_ = "Basic ";
    private static final String BEARER_ = "Bearer ";

    /**
     * *从header 请求中的clientId/clientsecect
     *
     * @param header
     * @return
     * @throws BusinessException
     */
    public static String[] extractAndDecodeHeader(String header)
            throws BusinessException {

        if (header == null || !header.startsWith(BEARER_)) {
            throw new BusinessException(BizExceptionEnum.EMPTY_TOKEN);
        }

        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(DECODE_TOKEN_FAIL,e);
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BusinessException(INVALID_BASIC_TOKEN);
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

}
