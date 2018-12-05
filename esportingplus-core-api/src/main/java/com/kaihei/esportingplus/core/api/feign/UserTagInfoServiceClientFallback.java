package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.UserTagInfoParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-12-04 17:41
 **/
public class UserTagInfoServiceClientFallback implements FallbackFactory<UserTagInfoServiceClient> {

    @Override
    public UserTagInfoServiceClient create(Throwable throwable) {
        return new UserTagInfoServiceClient() {

            @Override
            public ResponsePacket createTagInfo(UserTagInfoParam userTagInfoParam) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket updateTagInfo(UserTagInfoParam userTagInfoParam) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<PageInfo> getRecords(@RequestParam(value = "page", required = true) Integer page,
                                                       @RequestParam(value = "size", required = true) Integer size) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket checkTagNameIsExist(String tagName) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket getTagInfo(Integer tagId) {
                return ResponsePacket.onError();
            }
        };
    }
}
