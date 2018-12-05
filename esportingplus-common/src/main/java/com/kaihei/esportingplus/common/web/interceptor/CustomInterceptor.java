package com.kaihei.esportingplus.common.web.interceptor;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.tools.TokenParseUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
@ConditionalOnProperty(value = "custom.interceptor", havingValue = "true")
public class CustomInterceptor implements HandlerInterceptor {

    protected static CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 完成整个请求之后调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
    }

    /**
     * 进入controller方法之后，渲染视图之前调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object arg2, ModelAndView arg3) throws Exception {
    }

    /**
     * 进入controller方法之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object arg2) throws Exception {
        String authorization = request.getHeader("Authorization");
        //内部拦截器校验token为空的话说明是内部调用，网关已经处理了空token
        if(StringUtils.isNoneBlank(authorization)){
            String token = TokenParseUtils.parseToken(authorization);
            if (StringUtils.isNoneBlank(token)) {
                UserSessionContext userInfo = cacheManager
                        .get(RedisKey.UID_ACCESS_TOKEN + token, UserSessionContext.class);
                RequestContextHolder.currentRequestAttributes()
                        .setAttribute(CommonConstants.USER_INFO, userInfo, RequestAttributes.SCOPE_REQUEST);
            }

        }

        return true;
    }
}