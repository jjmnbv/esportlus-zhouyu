package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.feign.UserBindServiceClient;
import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.domain.service.MembersAuth3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bind")
@Api(tags = {"用户第三方账号绑定服务接口"})
public class UserBindController implements UserBindServiceClient {
    @Autowired
    private MembersAuth3Service membersAuth3Service;


    /**
     * 绑定第三方账号:wx/qq
     *
     * @param params
     */
    @ApiOperation(value = "绑定第三方账号:wx/qq")
    @Override
    public ResponsePacket auth3Bind(@RequestBody ThirdPartLoginParams params) {
        membersAuth3Service.bindAuth3(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 绑定手机号
     *
     * @param params
     */
    @ApiOperation(value = "绑定手机号")
    @Override
    public ResponsePacket phoneBind(@RequestBody PhoneBindParams params) {
        membersAuth3Service.bindPhone(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * H5绑定第三方账号:wx/qq
     *
     * @param params
     */
    @ApiOperation(value = "H5绑定第三方账号:wx/qq")
    @Override
    public ResponsePacket h5PhoneBind(@RequestBody PhoneBindParams params) {
        membersAuth3Service.bindPhone(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 获取绑定列表
     *
     * @param token
     */
    @ApiOperation(value = "获取绑定列表")
    @Override
    public ResponsePacket bindList(@RequestHeader(name = "Authorization", required = false) String token) {
        Map<String, String> bindMap = membersAuth3Service.bindList();
        return ResponsePacket.onSuccess(bindMap);
    }

    /**
     * 绑定手机号验证
     *
     * @param params
     * @param token
     */
    @ApiOperation(value = "绑定手机号验证")
    @Override
    public ResponsePacket verifyOldPhone(@RequestBody PhoneBindParams params, @RequestHeader(name = "Authorization", required = false) String token) {
        membersAuth3Service.verifyOldPhone(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 更改绑定手机号
     *
     * @param params
     * @param token
     */
    @ApiOperation(value = "更改绑定手机号")
    @Override
    public ResponsePacket updatePhone(@RequestBody PhoneBindParams params, @RequestHeader(name = "Authorization", required = false) String token) {
        membersAuth3Service.updatePhone(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 解绑第三方账号:wx/qq
     *
     * @param platform
     * @param token
     */
    @ApiOperation(value = "解绑第三方账号:wx/qq")
    @Override
    public ResponsePacket unbindAuth3(@PathVariable(name = "unbind_type") String platform, @RequestHeader(name = "Authorization", required = false) String token) {
        membersAuth3Service.unBindAuth3(platform);
        return ResponsePacket.onSuccess();
    }

    /**
     * 根据uid查询绑定微信unionid
     *
     * @param uids
     * @return
     * */
    @ApiOperation(value = "根据uid查询绑定微信unionid")
    @Override
    public ResponsePacket getUnionIdByUids(@RequestBody List<String> uids) {
        Map<String, String> resultMap = membersAuth3Service.getUnionIdByUids(uids);
        return ResponsePacket.onSuccess(resultMap);
    }
}
