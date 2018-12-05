package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersAlbumpicture;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MembersAlbumpictureRepository extends CommonRepository<MembersAlbumpicture> {

    List<MembersAlbumpicture> selectPicturesByUserId(@Param("statusList") List<Integer> status,
                                                     @Param("userId") Integer userId,@Param("pictureId") Integer pictureId);

    int pictureCountByUserId(@Param("statusList") List<Integer> status,
                         @Param("userId") Integer userId,@Param("pictureId") Integer pictureId);

    int updateUserIdWeights(@Param("statusList") List<Integer> status,
                            @Param("userId") Integer userId,@Param("weights") Integer weights);

    int updateStatusByUserId(@Param("userId")int userId, @Param("status") int status,@Param("id") int id);
}