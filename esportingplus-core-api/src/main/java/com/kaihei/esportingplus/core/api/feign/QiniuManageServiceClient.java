package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.QiniuQueryParam;
import com.kaihei.esportingplus.core.api.params.SmsSendParam;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.core.api.vo.QiniuTokenVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zl.zhao
 * @Description 七牛feign
 * @Date 2018/10/29 19:28
 **/
@FeignClient(name = "esportingplus-core-service", path = "/qiniu", fallbackFactory = QiniuManageServiceClientFallbackFactory.class)
public interface QiniuManageServiceClient {
    /**
     * 获取七牛token信息
     *
     * @param param
     */
    @PostMapping("/token/get")
    public ResponsePacket<QiniuTokenVo> getTokenByTokenType(@RequestBody QiniuQueryParam param);

    /**
     * 鉴暴恐
     *
     * @param param
     */
    @GetMapping("/image/check/qterror")
    public ResponsePacket<QiniuImageCheckVo> checkQterrorImage(@RequestParam(value = "param", required = true) String param);


    /**
     * 鉴黄
     *
     * @param param
     */
    @GetMapping("/image/check/qpulp")
    public ResponsePacket<QiniuImageCheckVo> checkQpulpImage(@RequestParam(value = "param", required = true) String param);

    /**
     * （首次注册）上传头像获取token
     *
     * @param
     */
    @GetMapping("/token/get/portrait")
    public ResponsePacket<QiniuTokenVo> getTokenByAvatar();

}
