package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.api.vo.AccountInfoVo;

public interface AttachService {

    public AccountInfoVo checkAccountInfo(String accountType, String userId, Integer amount) throws Exception;
}
