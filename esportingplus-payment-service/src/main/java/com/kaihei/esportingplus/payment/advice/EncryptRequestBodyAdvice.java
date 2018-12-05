package com.kaihei.esportingplus.payment.advice;

import com.kaihei.esportingplus.payment.annotation.DecryptData;
import com.kaihei.esportingplus.payment.config.EncryptConfig;
import com.kaihei.esportingplus.payment.util.RSAUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 请求请求处理类（目前仅仅对RequestBody 有效） 对加了@DecryptData 的方法的数据进行解密操作
 *
 * @author haycco
 */
@ControllerAdvice
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {

    private static final Logger logger = LoggerFactory.getLogger(EncryptRequestBodyAdvice.class);

    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage,
            MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (parameter.getMethod().isAnnotationPresent(DecryptData.class) && !encryptConfig.isDebug()) {
            try {
                return new DecryptHttpInputMessage(inputMessage, encryptConfig.getPrivateKey(), encryptConfig.getCharset());
            } catch (Exception e) {
                logger.error("数据解密失败", e);
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
            MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}

class DecryptHttpInputMessage implements HttpInputMessage {

    private HttpHeaders headers;
    private InputStream body;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String privateKey, String charset) throws Exception {

        if (StringUtils.isEmpty(privateKey)) {
            throw new IllegalArgumentException("private-key is null");
        }

        //获取请求内容
        this.headers = inputMessage.getHeaders();
        String content = IOUtils.toString(inputMessage.getBody(), charset);

        //未加密数据不进行解密操作
        String decryptBody;
        if (content.startsWith("{")) {
            decryptBody = content;
        } else {
            StringBuilder json = new StringBuilder();
            content = content.replaceAll(" ", "+");

            if (!StringUtils.isEmpty(content)) {
                String[] contents = content.split("\\|");
                for (int k = 0; k < contents.length; k++) {
                    String value = contents[k];
                    value = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decodeFromString(value), privateKey), charset);
                    json.append(value);
                }
            }
            decryptBody = json.toString();
        }
        this.body = IOUtils.toInputStream(decryptBody, charset);
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
