package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoniang;
import java.util.List;

public interface BaojiBaoniangRepository extends CommonRepository<BaojiBaoniang> {

    List<BaojiBaoniang> selectBaoniangInfoByUid(String uid);
}