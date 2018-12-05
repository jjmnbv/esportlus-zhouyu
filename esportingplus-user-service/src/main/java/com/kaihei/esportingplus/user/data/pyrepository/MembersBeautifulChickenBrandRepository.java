package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersBeautifulChickenBrand;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MembersBeautifulChickenBrandRepository extends
        CommonRepository<MembersBeautifulChickenBrand> {

    public List<String> selectAllChickenIds(@Param("offset") Integer offset,
            @Param("limit") Integer limit);

}
