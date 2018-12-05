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

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.SecurityConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.tools.TokenParseUtils;
import com.kaihei.esportingplus.gateway.server.config.FilterIgnorePropertiesConfig;
import com.kaihei.esportingplus.gateway.server.service.PermissionService;
import com.kaihei.esportingplus.gateway.server.utils.ZuulFilterUtils;
import com.kaihei.esportingplus.gateway.server.utils.ZuulResponseUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Orochi-Yzh
 * @Description: 接口访问前触发前置过滤器
 * @dateTime 2018/8/4 15:23
 */
@Component
public class PreAccessFilter extends ZuulFilter {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    @Autowired
    private PermissionService permissionService;

    @Value("${python.uid.mock}")
    private boolean mock;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 30;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = TokenParseUtils
                .parseToken(request.getHeader(SecurityConstants.AUTHORIZATION));

        //1.校验不需要认证的接口路径
        boolean anyMatch = ZuulFilterUtils
                .checkIgnore(request.getRequestURI(), filterIgnorePropertiesConfig);
        if(anyMatch){
            return false;
        }

        //如果超出限流和黑名单取消后续处理
        if (ctx.remove("blackList:" + token) != null
                || ctx.remove("activeRateLimit:" + token) != null) {
            return false;
        }

        //如果找不到对应的refresh_toekn，说明token非法
        LOGGER.debug("token: {}", token);
        if (StringUtils.isBlank(token)) {
            LOGGER.error(BizExceptionEnum.EMPTY_TOKEN.getErrMsg());
            ZuulResponseUtils.failJson(ctx, ResponsePacket.onError(HttpStatus.SC_UNAUTHORIZED,BizExceptionEnum.EMPTY_TOKEN.getErrMsg()),
                    HttpStatus.SC_UNAUTHORIZED);
            return false;
        }
        String refreshToken = cacheManager.get(RedisKey.ACCESS_TO_REFRESH + token, String.class);
        if(refreshToken == null){
            ZuulResponseUtils
                    .failJson(ctx, ResponsePacket.onError(BizExceptionEnum.INVALID_TOKEN),
                            HttpStatus.SC_UNAUTHORIZED);
            LOGGER.error("refreshToken为空，此token非法");
            return false;
        }

        return true;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = TokenParseUtils
                .parseToken(request.getHeader(SecurityConstants.AUTHORIZATION));

        //token是否过期
        try {

            //如果access_token过期,传参到后置过滤器进行刷新
            boolean hashAccessToken = cacheManager.exists(RedisKey.ACCESS + token);
            if (!hashAccessToken) {
                //获取已过期access_token的refreshToken
                String refreshToken = cacheManager
                        .get(RedisKey.ACCESS_TO_REFRESH + token, String.class);
                ctx.set("refreshToken" + request.getHeader(SecurityConstants.AUTHORIZATION),
                        refreshToken);
            } else {
                //正常访问，进行鉴权
                ResponsePacket hasPermisstion = permissionService
                        .hasPermission(token, request.getRequestURI());
                if (hasPermisstion.getCode() != BizExceptionEnum.SUCCESS.getErrCode()) {
                    ZuulResponseUtils.failJson(ctx, hasPermisstion, HttpStatus.SC_FORBIDDEN);
                    return null;
                }
                //正常访问，token没过期 返回旧token
                ctx.getResponse().setHeader(SecurityConstants.AUTHORIZATION,
                        request.getHeader(SecurityConstants.AUTHORIZATION));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            if(e instanceof HttpClientErrorException){
                HttpClientErrorException exception = (HttpClientErrorException) e;
                ZuulResponseUtils
                        .fail(ctx, exception.getResponseBodyAsString(), exception.getRawStatusCode());
            }else{
                ZuulResponseUtils
                        .failJson(ctx, ResponsePacket.onError(BizExceptionEnum.INTERNAL_SERVER_ERROR)
                                , HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        }

        return null;
    }

}
