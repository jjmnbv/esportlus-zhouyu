package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigAppVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ResourceStateConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResourceStateConfigServiceClientFallbackFactory implements
        FallbackFactory<ResourceStateConfigServiceClient> {

    @Override
    public ResourceStateConfigServiceClient create(Throwable throwable) {
        return new ResourceStateConfigServiceClient() {


            @Override
            public ResponsePacket<List<ResourceStateConfigVo>> findResourceStateConfig(
                    Integer userType,
                    String position) {
                return null;
            }

            @Override
            public ResponsePacket<ResourceStateConfigVo> findResourceStateConfigById(
                    Integer resourceId) {
                return null;
            }

            @Override
            public ResponsePacket<Void> saveResourceStateConfig(
                    ResourceStateSaveParams resourceStateSaveParams) {
                return null;
            }

            @Override
            public ResponsePacket<Void> updateResourceStateConfig(
                    ResourceStateUpdateParams resourceStateUpdateParams) {
                return null;
            }

            @Override
            public ResponsePacket<Void> deleteResourceStateConfig(Integer id) {
                return null;
            }
        };
    }
}
