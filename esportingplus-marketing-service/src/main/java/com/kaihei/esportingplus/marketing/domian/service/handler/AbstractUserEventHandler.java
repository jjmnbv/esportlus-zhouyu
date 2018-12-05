package com.kaihei.esportingplus.marketing.domian.service.handler;

import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.api.event.UserEvent;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 用户数据handler抽象类
 *
 * @author xiekeqing
 */
public abstract class AbstractUserEventHandler<T extends UserEvent> implements UserEventHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean handle(UserEvent userEvent) {
        String json = JacksonUtils.toJson(userEvent);
        logger.debug("{} >> start >> handle : {}", getClass(), json);

        T t = parseUserEvent(userEvent);
        if (t != null) {
            return process(t);
        }

        logger.debug("{} >> end >> handle : {}", getClass(), json);

        return true;
    }

    public abstract boolean process(T t);

    /**
     * 将用户事件解析为具体事件实现类
     *
     * @return T
     */
    protected T parseUserEvent(UserEvent userEvent) {
        if (userEvent != null) {
            Type type = getUserEventType();
            if (type instanceof Class) {
                try {
                    T data = (T) userEvent;
                    return data;
                } catch (Exception e) {
                    logger.error("parse userEvent type fail : {}", e.getMessage());
                }
            } else {
                String json = JacksonUtils.toJson(userEvent);
                logger.warn("parse userEvent error. {}", json);
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 获取当前事件handler实现类处理的事件类型
     *
     * @return Type
     */
    protected Type getUserEventType() {
        Type superType = getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
            return actualTypeArguments[0];
        } else {
            return Object.class;
        }
    }

    protected class NumberHolder {
        /**
         * 启用/不启用
         */
        private Boolean enable = false;
        private Integer number = null;

        public NumberHolder() {
        }

        public NumberHolder(Integer number) {
            this.number = number;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }
    }

}
