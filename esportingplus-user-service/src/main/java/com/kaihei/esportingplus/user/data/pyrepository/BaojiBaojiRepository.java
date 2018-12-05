package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoJiTag;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoji;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaojiBaojiRepository extends CommonRepository<BaojiBaoji> {

    List<BaojiBaoji> selectBaojiByUidStatus(@Param("uid") String uid, @Param("status") short status);

    BaojiBaoJiTag getBaoJiMaxLevel(@Param("uid") String uid, @Param("status")short status);

    BaojiBaoJiTag getBaoJiLevelByGame(@Param("uid") String uid,
                                      @Param("game")Integer gameCode,
                                      @Param("status")short status);
}