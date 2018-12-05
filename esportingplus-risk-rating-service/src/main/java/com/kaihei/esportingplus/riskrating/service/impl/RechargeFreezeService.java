package com.kaihei.esportingplus.riskrating.service.impl;

import com.kaihei.esportingplus.riskrating.api.params.RechargeFreezedUserFindParams;
import com.kaihei.esportingplus.riskrating.api.params.UserRechargeFreezeParams;
import com.kaihei.esportingplus.riskrating.api.vo.RechargeFreezeUserVO;
import com.kaihei.esportingplus.riskrating.domain.entity.RechargeFreeze;
import com.kaihei.esportingplus.riskrating.repository.RechargeFreezeRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * 用户充值风控相关服务实现类
 *
 * @author 谢思勇
 */
@Service
public class RechargeFreezeService {

    @Resource
    private RechargeFreezeRepository rechargeFreezeRepository;


    /**
     * 冻结用户充值功能
     */
    @Transactional(rollbackOn = Exception.class)
    public boolean freezeUserRecharge(UserRechargeFreezeParams userRechargeFreezeParams) {
        RechargeFreeze rechargeFreeze = userRechargeFreezeParams.cast(RechargeFreeze.class);
        boolean exists = rechargeFreezeRepository.exists(Example.of(rechargeFreeze));
        if (exists) {
            return true;
        }
        rechargeFreezeRepository.save(rechargeFreeze);
        return true;
    }

    /**
     * 移除用户充值冻结
     */
    @Transactional(rollbackOn = Exception.class)
    public boolean unfreezeUserRecharge(String uid) {
        rechargeFreezeRepository.deleteByUid(uid);
        return true;
    }

    public boolean isRechargeFreezed(String uid) {
        RechargeFreeze rechargeFreeze = new RechargeFreeze();
        rechargeFreeze.setUid(uid);

        return rechargeFreezeRepository.exists(Example.of(rechargeFreeze));
    }

    /**
     * 查询充值功能被冻结的用户
     */
    public Map<String, Integer> rechargeFreezedUsers(List<String> uids) {
        List<RechargeFreeze> rechargeFreezes = rechargeFreezeRepository.findByUidIn(uids);
        List<String> freezedUids = rechargeFreezes.stream().map(RechargeFreeze::getUid)
                .collect(Collectors.toList());

        //返回一个uid 和 是否被冻结的映射
        return uids.stream().collect(Collectors.toMap(e -> e, e -> {
            if (freezedUids.contains(e)) {
                return 1;
            }
            return 0;
        }));
    }

    public Page<RechargeFreezeUserVO> findFreezedUsersPage(
            RechargeFreezedUserFindParams rechargeFreezedUserFindParams) {
        //参数调整
        if (rechargeFreezedUserFindParams.getPageSize() == 0) {
            rechargeFreezedUserFindParams.setPageSize(Integer.MAX_VALUE);
        }
        if (rechargeFreezedUserFindParams.getPage() != 0) {
            rechargeFreezedUserFindParams.setPage(rechargeFreezedUserFindParams.getPage() - 1);
        }

        PageRequest pageRequest = new PageRequest(rechargeFreezedUserFindParams.getPage(),
                rechargeFreezedUserFindParams.getPageSize());

        Page<RechargeFreeze> page = rechargeFreezeRepository
                .findByUidContaining(rechargeFreezedUserFindParams.getUid(),
                        pageRequest);

        //转成VO
        List<RechargeFreeze> content = page.getContent();
        List<RechargeFreezeUserVO> contentVO = content.stream()
                .map(it -> it.cast(RechargeFreezeUserVO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(contentVO, pageRequest, page.getTotalElements());
    }
}
