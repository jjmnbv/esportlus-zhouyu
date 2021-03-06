/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   ohun@live.cn (夜色)
 */

package com.kaihei.esportingplus.common.event;

import com.google.common.eventbus.AsyncEventBus;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件总线，非分布式，仅适用于单进程实例内
 * @author LiuQing.Qin
 * @date 2018/4/20 13:29
 */
public class EventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);
    private static com.google.common.eventbus.EventBus eventBus;

    public static void create(Executor executor) {
        eventBus = new AsyncEventBus(executor, (exception, context)
                -> LOGGER.error("event bus subscriber ex", exception));
    }

    public static void post(Event event) {
        eventBus.post(event);
    }

    public static void register(Object bean) {
        eventBus.register(bean);
    }

    public static void unregister(Object bean) {
        eventBus.unregister(bean);
    }

}
