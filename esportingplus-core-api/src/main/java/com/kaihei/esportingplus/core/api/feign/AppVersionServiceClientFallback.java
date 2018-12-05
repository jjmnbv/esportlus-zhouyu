package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.PageParams;
import com.kaihei.esportingplus.core.api.vo.AppVersionChangelogVo;
import com.kaihei.esportingplus.core.api.vo.AppVersionNotifyVo;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author yangshidong
 * @date 2018/12/1
 * */
@Component
public class AppVersionServiceClientFallback implements FallbackFactory<AppVersionServiceClient> {
    @Override
    public AppVersionServiceClient create(Throwable cause) {
        return new AppVersionServiceClient() {
            @Override
            public ResponsePacket saveVersionLog(AppVersionChangelogVo appVersionChangelogVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateVersionLog(AppVersionChangelogVo appVersionChangelogVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket enableVersionLog(int versionLogId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket disableVersionLog(int versionLogId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket deleteVersionLog(int versionLogId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PageInfo> getVersionLogList(PageParams pageParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<AppVersionNotifyVo> notifyAppUpdate(short clientType, String version) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}
