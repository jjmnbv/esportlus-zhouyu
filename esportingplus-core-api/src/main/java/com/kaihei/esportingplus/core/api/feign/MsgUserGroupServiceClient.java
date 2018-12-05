package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.GroupDismissParam;
import com.kaihei.esportingplus.core.api.params.GroupJoinParam;
import com.kaihei.esportingplus.core.api.params.GroupQuitParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/26 11:28
 **/
@FeignClient(name = "esportingplus-core-service", path = "/message/group", fallbackFactory = MsgUserGroupServiceClientFallback.class)
public interface MsgUserGroupServiceClient {


    /**
     * 创建群组
     * @param groupCreateParam
     */
    @PostMapping("/create")
    ResponsePacket<Boolean> createGroup(@RequestBody GroupJoinParam groupCreateParam);

    /**
     * 加入群组
     * @param groupCreateParam
     */
    @PostMapping("/join")
    ResponsePacket<Boolean> joinGroup(@RequestBody GroupJoinParam groupCreateParam);

    /**
     * 退出群组
     * @param groupQuitParam
     */
    @PostMapping("/quit")
    ResponsePacket<Boolean> leaveGroup(@RequestBody GroupQuitParam groupQuitParam);

    /**
     * 解散群组
     * @param groupDismissParam
     */
    @PostMapping("/dismiss")
    ResponsePacket<Boolean> dismissGroup(@RequestBody GroupDismissParam groupDismissParam);

}
