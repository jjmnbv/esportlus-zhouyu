package com.kaihei.esportingplus.resource.data.manager;

import com.kaihei.esportingplus.api.params.BaojiCertifyParams;
import com.kaihei.esportingplus.api.vo.BaojiCertifyResult;

/**
 * @author liangyi
 * 调用 Python 接口获取数据
 */
public interface PythonSupportService {

    /**
     * 调用 Python 接口查询暴鸡认证信息
     * @param baojiCertifyParams
     * @return
     */
    BaojiCertifyResult getBaojiCertifyInfo(BaojiCertifyParams baojiCertifyParams);
}
