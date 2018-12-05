package com.kaihei.esportingplus.gateway.server.filter;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.SecurityConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.tools.TokenParseUtils;
import com.kaihei.esportingplus.gateway.server.data.manager.AuthServerRestClient;
import com.kaihei.esportingplus.gateway.server.utils.ZuulResponseUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Orochi-Yzh
 * @Description: 对过期的token进行刷新，第一次有效，客户端需要拿新的token请求接口，否则报token非法
 * @dateTime 2018/8/4 15:23
 */
@Component
public class AffterAccessFilter extends ZuulFilter {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthServerRestClient authServerRestClient;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 31;
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

        Object refreshToken = ctx
                .get("refreshToken" + request.getHeader(SecurityConstants.AUTHORIZATION));
        //参数有值
        if (refreshToken != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String pythonToken = request.getHeader(SecurityConstants.AUTHORIZATION);
        String version = request.getHeader("x");

        //取出参数并删除，防止内存资源消耗
        Object refreshToken = ctx
                .remove("refreshToken" + request.getHeader(SecurityConstants.AUTHORIZATION));

        //开始刷新token
        try {
            ResponsePacket<String> resultResponse = authServerRestClient
                    .refreshToken(pythonToken.toString(), version);
            //toeken刷新成功，重置token 并返回新token到header
            if (resultResponse != null && StringUtils.isNoneBlank(resultResponse.getData())) {
                LOGGER.debug("token 刷新成功，重置客户端token为{}", resultResponse.getData());
                ctx.getResponse()
                        .setHeader(SecurityConstants.ACCESS_TOKEN, resultResponse.getData());
            } else {
                ZuulResponseUtils
                        .failJson(ctx, ResponsePacket.onError(HttpStatus.SC_UNAUTHORIZED,
                                BizExceptionEnum.REFRESH_TOKEN_ERROR.getErrMsg()),
                                HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return null;
            }

        }catch (Exception e){
            //捕获pt token校验失败异常
            if(e instanceof HttpClientErrorException){
                HttpClientErrorException exception = ((HttpClientErrorException)e);
                ResponsePacket authResult = FastJsonUtils
                        .fromJson(exception.getResponseBodyAsString(), ResponsePacket.class);
                if (authResult!= null && authResult.getCode() != BizExceptionEnum.SUCCESS.getErrCode()) {
                    //auth 服务返回400，重置为401
                    authResult.setCode(HttpStatus.SC_UNAUTHORIZED);
                    ZuulResponseUtils.failJson(ctx, authResult, HttpStatus.SC_UNAUTHORIZED);
                    return null;
                }
            }else{
                ZuulResponseUtils
                        .failJson(ctx, ResponsePacket.onError(BizExceptionEnum.INTERNAL_SERVER_ERROR)
                                , HttpStatus.SC_INTERNAL_SERVER_ERROR);

                return null;
            }
        }

        return null;
    }

}
