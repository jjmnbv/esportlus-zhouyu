package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.BaoJiBnVerifypic;

public interface BaoJiBnVerifypicRepository extends CommonRepository<BaoJiBnVerifypic> {
    BaoJiBnVerifypic selectPictureByUid(String uid);
}