/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.kaihei.esportingplus.gateway.server.filter;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.SecurityConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.TokenParseUtils;
import com.kaihei.esportingplus.gateway.server.exception.ZuulFilterException;
import com.kaihei.esportingplus.gateway.server.utils.ZuulResponseUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author Orochi-Yzh
 * @Description: 认证通过之后： 1.把access_token设置反reponse header 中返回给前端
 * @dateTime 2018/8/6 10:02
 */
@Component
public class AffterAuthFilter extends ZuulFilter {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 29;
    }


    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = TokenParseUtils
                .parseToken(request.getHeader(SecurityConstants.AUTHORIZATION));
        //如果超出限流取消后续处理
        if (ctx.remove("blackList:" + token) != null
                || ctx.remove("activeRateLimit:" + token) != null) {
            return false;
        }
        //拦截认证地址（/oauth/token),只有认证地址 才会通过
        String uri = request.getRequestURI();
//        return StringUtils.containsAnyIgnoreCase(uri,
//                "/api/user/auth");
        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            //TODO auth服务统一异常
            String result = new BufferedReader(new InputStreamReader(ctx.getResponseDataStream()))
                    .lines().parallel().collect(Collectors.joining(System.lineSeparator()));

            LOGGER.info("登录认证结果:{}", result);
            ZuulFilterException authResultError = FastJsonUtils
                    .fromJson(result, ZuulFilterException.class);
            if (authResultError.getError() != null) {
                ZuulResponseUtils.failJson(ctx, ResponsePacket.onError(HttpStatus.SC_UNAUTHORIZED
                        , authResultError.getMessage())
                        , authResultError.getStatus());
                return null;
            }

            //TODO 内部异常问题
            ResponsePacket authResult = FastJsonUtils.fromJson(result, ResponsePacket.class);
            if (authResult.getCode() != BizExceptionEnum.SUCCESS.getErrCode()) {
                //auth 服务返回400，重置为401
                LOGGER.info("auth 异常重置为401");
                authResult.setCode(HttpStatus.SC_UNAUTHORIZED);
                ZuulResponseUtils.failJson(ctx, authResult, HttpStatus.SC_UNAUTHORIZED);
                return null;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }

        return null;
    }

}
