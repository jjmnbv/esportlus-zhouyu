package com.kaihei.esportingplus.payment.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取客户端真实IP地址
 * 
 * @author haycco
 *
 */
public class IpUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);
    
    private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For",
                                                           "Proxy-Client-IP",
                                                           "WL-Proxy-Client-IP",
                                                           "HTTP_X_FORWARDED_FOR",
                                                           "HTTP_X_FORWARDED",
                                                           "HTTP_X_CLUSTER_CLIENT_IP",
                                                           "HTTP_CLIENT_IP",
                                                           "HTTP_FORWARDED_FOR",
                                                           "HTTP_FORWARDED",
                                                           "HTTP_VIA",
                                                           "REMOTE_ADDR" };

    public static String getIp(HttpServletRequest request) {
        
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            logger.debug(header + ": " + ip);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                if ("0:0:0:0:0:0:0:1".equalsIgnoreCase(ip) || "localhost".equalsIgnoreCase(ip)) {
                    return "127.0.0.1";
                }
                if(header.equalsIgnoreCase("X-Forwarded-For")){
                    return ip.split(",")[0];
                }
                return ip;
            }
        }
        return request.getRemoteAddr();
        
    }
}
