package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.params.MPPushParam;
import com.kaihei.esportingplus.core.api.params.MpFormUploadParam;

import java.util.List;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/11/5 14:34
 **/
public interface MpPushService {

   Boolean push(List<MPPushParam> params);

   boolean upload(MpFormUploadParam param);
}
