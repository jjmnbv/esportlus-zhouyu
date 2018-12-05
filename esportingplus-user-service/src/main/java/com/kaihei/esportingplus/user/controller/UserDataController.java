package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.feign.UserDataServiceClient;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.domain.service.MembersOrderCountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userdata")
@Api(tags = {"用户基础服务接口"})
public class UserDataController implements UserDataServiceClient {

    @Autowired
    private MembersOrderCountService membersOrderCountService;


    @Override
    public ResponsePacket incrUserFreeData(@RequestParam(value = "acceptList", required = true) List<String> acceptList,
                                           @RequestParam(value = "placeList", required = true) List<String> placeList) {
        membersOrderCountService.incrUserFreeData(acceptList,placeList);
        return ResponsePacket.onSuccess();
    }
}
