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

package com.kaihei.esportingplus.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;

/**
 * esportingplus 全局配置
 *
 * @author LiuQing.Qin
 */
public interface GlobalConfig {
    /*Config cfg = load();

    static Config load() {
        Config config = ConfigFactory.load();//扫描加载所有可用的配置文件
        String custom_conf = "application.properties";//加载自定义配置, 值来自jvm启动参数指定-Dmp.conf
        if (config.hasPath(custom_conf)) {
            File file = new File(config.getString(custom_conf));
            if (file.exists()) {
                Config custom = ConfigFactory.parseFile(file);
                config = custom.withFallback(config);
            }
        }
        return config;
    }


    public static void main(String[] args) {
        System.out.println(GlobalConfig.load());
    }*/
}