package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.api.vo.BaoJiCelebrityVo;
import com.kaihei.esportingplus.user.domain.entity.BaoJiCelebrity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaoJiCelebrityRepository extends CommonRepository<BaoJiCelebrity> {
    List<BaoJiCelebrityVo> selectCelebrityList(@Param("game") Integer game);
}