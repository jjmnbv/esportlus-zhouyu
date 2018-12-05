//package com.kaihei.esportingplus.riskrating.filter;
//
//import com.kaihei.esportingplus.common.ResponsePacket;
//import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
//import com.kaihei.esportingplus.common.tools.JacksonUtils;
//import com.kaihei.esportingplus.common.web.interceptor.CustomInterceptor;
//import com.kaihei.esportingplus.riskrating.service.RiskDictService;
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
///**
// * 风控全局总开关
// *
// * @author haycco
// */
//@Component
//@ConditionalOnBean(CustomInterceptor.class)
//public class GlobalSwitchPerRequestFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(GlobalSwitchPerRequestFilter.class);
//    //全局配置总开关URI前缀
//    private static final String GLOBAL_CONFIG_REQUEST_URI = "/config/";
//
//    @Autowired
//    private RiskDictService riskDictService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        boolean riskSwitch = riskDictService.checkRiskSwitchStatus();
//        logger.info("系统风控总开关值为：{}", riskSwitch);
//        String requestURL = request.getRequestURI();
//        boolean isConfigRequest = !StringUtils.isEmpty(requestURL) && requestURL.startsWith(GLOBAL_CONFIG_REQUEST_URI) && request.getMethod().equals(HttpMethod.PUT.name());
//        // 风控不启用，直接返回，设置配置类请求除外
//        if(!riskSwitch && !isConfigRequest){
//            ResponsePacket result = ResponsePacket.onSuccess();
//            result.setCode(BizExceptionEnum.GLOBAL_RISK_SWITCH_CLOSED.getErrCode());
//            result.setMsg(BizExceptionEnum.GLOBAL_RISK_SWITCH_CLOSED.getErrMsg());
//            result.setData(null);
//            logger.warn("风控统一返回：{}", result);
//
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType(MediaType.APPLICATION_JSON.toString());
//            response.setStatus(HttpStatus.OK.value());
//            response.getWriter().print(JacksonUtils.toJson(result));
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }
//
//}
