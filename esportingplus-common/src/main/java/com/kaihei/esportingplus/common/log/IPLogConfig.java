package com.kaihei.esportingplus.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.kaihei.esportingplus.common.tools.IpAddressUtil;
import java.net.UnknownHostException;

/**
 *@Description: 在logback日志中加入 ip地址:第一块网卡的IP地址
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/9/27 18:05
*/
public class IPLogConfig extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return IpAddressUtil.getInetAddress(true);
    }
}