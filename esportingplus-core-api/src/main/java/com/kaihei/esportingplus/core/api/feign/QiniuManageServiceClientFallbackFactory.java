package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.QiniuQueryParam;
import com.kaihei.esportingplus.core.api.params.SmsSendParam;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.core.api.vo.QiniuTokenVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author zl.zhao
 * @Description
 * @Date 2018/10/29 19:28
 **/
@Component
public class QiniuManageServiceClientFallbackFactory implements FallbackFactory<QiniuManageServiceClient> {
    @Override
    public QiniuManageServiceClient create(Throwable throwable) {
        return new QiniuManageServiceClient() {
            @Override
            public ResponsePacket<QiniuTokenVo> getTokenByTokenType(QiniuQueryParam param) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<QiniuImageCheckVo> checkQterrorImage(String param) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<QiniuImageCheckVo> checkQpulpImage(String param) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<QiniuTokenVo> getTokenByAvatar() {
                return ResponsePacket.onHystrix();
            }
        };
    }
}
