//package com.kaihei.esportingplus.riskrating.interceptor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
//import com.kaihei.esportingplus.riskrating.api.enums.FreeTeamEnum;
//import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
//import com.kaihei.esportingplus.riskrating.service.RiskDictService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//
///**
// * 风控开关-拦截
// * @author chenzhenjun
// */
//public class RiskSwitchInterceptor implements HandlerInterceptor {
//
//    private static final Logger logger = LoggerFactory.getLogger(RiskSwitchInterceptor.class);
//
//    @Autowired
//    private RiskDictService riskDictService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        boolean riskSwitch = riskDictService.checkRiskSwitchStatus();
//        logger.info("系统风控开关值为：【{}】", riskSwitch);
//        // 风控不启用，直接返回
//        if(!riskSwitch){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("code", BizExceptionEnum.SUCCESS.getErrCode());
//            jsonObject.put("msg", BizExceptionEnum.SUCCESS.getErrMsg());
//            FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
//            jsonObject.put("data", response);
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            httpServletResponse.setContentType("text/html;charset=UTF-8");
//            PrintWriter out = httpServletResponse.getWriter();
//            out.print(jsonObject.toString());
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//    }
//}
