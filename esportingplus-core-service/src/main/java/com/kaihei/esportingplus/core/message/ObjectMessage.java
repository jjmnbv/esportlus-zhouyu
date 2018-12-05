package com.kaihei.esportingplus.core.message;

public interface ObjectMessage<T> extends Message {
    T getObject();
}
