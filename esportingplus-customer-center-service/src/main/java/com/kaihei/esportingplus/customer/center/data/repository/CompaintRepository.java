package com.kaihei.esportingplus.customer.center.data.repository;

import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.params.StudioComplainQueryParams;
import com.kaihei.esportingplus.api.vo.ComplaintDetailVo;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.api.vo.StudioComplaintListVo;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.Compaint;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompaintRepository extends CommonRepository<Compaint> {
    void insertCompint(Compaint compaint);

    Integer checkUniqueCompaint(@Param("uid") String uid, @Param("beUid") String beUid);

    List<ComplaintListVo> listCompaint(ComplaintQueryParam complaintQueryParam);

    List<StudioComplaintListVo> studiolist(StudioComplainQueryParams studioComplainQueryParams);

    ComplaintDetailVo selectCompaintById(@Param("oid") Integer oid);

    List<ComplaintListVo> selectCompaintByOrderSequeues(@Param("sequeues") List<String> sequeues);

    Integer selectCountByBeuidAndStatusLte(@Param("beUid") String beUid,
            @Param("status") int status);
}