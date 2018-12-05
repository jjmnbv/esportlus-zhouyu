package com.kaihei.esportingplus.api.jsonserialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kaihei.esportingplus.api.enums.ComplaintStatusEnum;
import java.io.IOException;

/**
 * 自定义投诉状态的序列化转化器
 * 将 投诉状态 序列化成 字符串
 * 如果需要反序列化则使用 JsonDeserializer
 * @author liangyi
 */
public class ComplaintStatusSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer status, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        String descByCode = ComplaintStatusEnum.getDescByCode(status);
        jsonGenerator.writeString(descByCode);
    }
}
