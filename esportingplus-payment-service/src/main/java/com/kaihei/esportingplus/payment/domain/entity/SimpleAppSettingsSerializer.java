package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Set;

/**
 * 支付应用信息表序列化简化
 * 
 * @author haycco
 */
public class SimpleAppSettingsSerializer extends JsonSerializer<Set<AppSetting>> {
    
    @Override
    public void serialize(Set<AppSetting> appSettings, JsonGenerator jsonGenerator,
            SerializerProvider serializers) throws IOException {
        
        jsonGenerator.writeStartArray();
        for (AppSetting appSetting : appSettings) {
            AppSetting temp = new AppSetting();
            temp.setId(appSetting.getId());
            temp.setAppId(appSetting.getAppId());
            temp.setAppName(appSetting.getAppName());
            temp.setState(appSetting.getState());
            jsonGenerator.writeObject(temp);
        }
        jsonGenerator.writeEndArray();

    }
}