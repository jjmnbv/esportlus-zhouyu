package com.kaihei.esportingplus.resource.data.manager.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DictCacheable {

    String value();
}
