package com.kaihei.esportingplus.payment.util;

import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.enums.AppTypeEnum;
import com.kaihei.esportingplus.payment.api.enums.SourceType;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 根据订单号生成一个分布式事务 id
     *
     * @author xusisi
     * @create 2018-09-25 16:21
     */
    public static String genTransactionId(String format, String teamSequence) {
        String now = DateUtil.dateTime2Str(LocalDateTime.now(), DateUtil.SIMPLE_FORMATTER);
        return String.format(format, teamSequence, now);
    }

    /**
     * 得到redis锁
     *
     * @param lockName
     * @return
     * @Author: zhouyu
     */
    public static RLock getRedisLock(String lockName) {
        RedissonClient redissonClient = CacheManagerFactory.create().redissonClient();
        return redissonClient.getLock(lockName);
    }

    /**
     * @Description: 发送HttpPost请求 json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     * @Param: [strURL, params]
     * @return: java.lang.String 成功:返回json字符串<br/>
     * @Author: xusisi
     * @Date: 2018/8/20
     */
    public static String postJson(String strURL, String params) throws Exception {
        // 创建连接
        URL url = new URL(strURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        // 设置连接超时为30s
        connection.setConnectTimeout(30000);
        // 读取数据超时也是30s
        connection.setReadTimeout(30000);
        connection.setInstanceFollowRedirects(true);
        // 设置请求方式
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Charset", "UTF-8");
        // 设置接收数据的格式
        connection.setRequestProperty("Accept", "application/json");
        // 设置发送数据的格式
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        // utf-8编码
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        out.append(params);
        out.flush();
        out.close();
        // 读取响应
        // 获取长度
        int length = (int) connection.getContentLength();
        if (length <= 0) {
            return null;
        }

        InputStream is = connection.getInputStream();
        byte[] data = new byte[length];
        byte[] temp = new byte[512];
        int readLen = 0;
        int destPos = 0;
        while ((readLen = is.read(temp)) > 0) {
            System.arraycopy(temp, 0, data, destPos, readLen);
            destPos += readLen;
        }
        // utf-8编码
        return new String(data, "UTF-8");
    }

    public static Integer strToInteger(String amountStr) throws BusinessException {
        Integer intAmount = 0;
        try {
            intAmount = Integer.valueOf(amountStr);

            if (intAmount < 1) {
                throw new BusinessException(BizExceptionEnum.INPUT_PARAM_NUMBER_ERROR);
            }
        } catch (Exception e) {
            throw new BusinessException(BizExceptionEnum.INPUT_PARAM_NUMBER_ERROR);
        }
        return intAmount;
    }

    public static String getSourceId(String appId) {

        String sourceId = null;
        //IOS_BJDJ=AppStore应用-暴鸡电竞
        //IOS_BJ=AppStore应用-暴鸡
        //ANDROID_BJDJ=Android应用-暴鸡电竞
        //WECHAT_PA_BJDJ=微信公众号应用-暴鸡电竞
        //WECHAT_MP_BJDJ=微信小程序应用-暴鸡电竞
        //H5=暴鸡电竞
        AppTypeEnum appTypeEnum = AppTypeEnum.lookup(appId);
        switch (appTypeEnum) {
            case H5:
                sourceId = SourceType.H5.getCode();
                break;
            case IOS_BJDJ:
            case IOS_BJ:
                sourceId = SourceType.IOS.getCode();
                break;
            case WECHAT_MP_BJDJ:
                sourceId = SourceType.MP.getCode();
                break;
            case ANDROID_BJDJ:
                sourceId = SourceType.ANDROID.getCode();
                break;
            case WECHAT_PA_BJDJ:
                sourceId = SourceType.PUBLIC_ACCOUNT.getCode();
                break;
            default:
                break;
        }
        if (StringUtils.isEmpty(sourceId)) {
            logger.error("exception : {}", BizExceptionEnum.APPID_AND_SOURCE_IS_NOT_MATCH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.APPID_AND_SOURCE_IS_NOT_MATCH);
        }
        return sourceId;
    }

}
