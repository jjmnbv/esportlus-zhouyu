package com.kaihei.esportingplus.payment.advice;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.kaihei.esportingplus.payment.annotation.EncryptData;
import com.kaihei.esportingplus.payment.config.EncryptConfig;
import com.kaihei.esportingplus.payment.util.RSAUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 请求响应处理类，对加了@EncryptData 的方法的数据进行加密操作
 *
 * @author haycco
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EncryptConfig encryptConfig;

    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();

    public static void setEncryptStatus(boolean status) {
        encryptLocal.set(status);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 可以通过调用EncryptResponseBodyAdvice.setEncryptStatus(false);来动态设置不加密操作
        Boolean status = encryptLocal.get();
        if (status != null && status == false) {
            encryptLocal.remove();
            return body;
        }

        boolean encrypt = false;
        if (returnType.getMethod().isAnnotationPresent(EncryptData.class) && !encryptConfig.isDebug()) {
            encrypt = true;
        }
        if (encrypt) {
            String privateKey = encryptConfig.getPrivateKey();
            try {
                // 解析返回数据节点
                JsonNode root = objectMapper.valueToTree(body);
                //有配置具体加密字段
                if (ArrayUtils.isNotEmpty(returnType.getMethod().getAnnotation(EncryptData.class).value())) {
                    for (String node : returnType.getMethod().getAnnotation(EncryptData.class).value()) {
                        //writeValueAsString出来的值是字符串带有双引号，此处需替换为空
                        String nodeContent = objectMapper.writeValueAsString(root.at(node)).replaceAll("\"", "");
                        byte[] nodeData = nodeContent.getBytes();
                        byte[] encodedNodeData = RSAUtils.encryptByPrivateKey(nodeData, privateKey);
                        String nodeResult = Base64Utils.encodeToString(encodedNodeData);
                        String lastFieldName = JsonPointer.compile(node).last().toString();
                        //反向查找需要替换加密值的父级属性名
                        String parentFieldName = node.substring(0, node.lastIndexOf(lastFieldName, node.length() - 1));
                        JsonNode parent = root.at(parentFieldName);
                        //需要替换的属性名需去掉xpath写法的斜杠
                        ((ObjectNode) parent).replace(lastFieldName.replace("/", ""), new TextNode(nodeResult));
                    }
                    return root;
                } else {
                    String content = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(body);
                    if (!StringUtils.hasText(privateKey)) {
                        throw new NullPointerException("请配置 spring.encrypt.private-key 参数");
                    }
                    byte[] data = content.getBytes();
                    byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
                    String result = Base64Utils.encodeToString(encodedData);
                    return result;
                }
            } catch (Exception e) {
                logger.error("加密数据异常", e);
            }
        }

        return body;
    }

}
