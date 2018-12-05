package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.domain.entity.MessageTemplate;
import org.springframework.core.convert.converter.Converter;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/29 14:12
 **/
public interface MessagePublish {

    boolean send(String from, Receiver to , Object message);
}
