package com.kaihei.esportingplus.user.assembler;

import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.user.api.vo.UserGameBaseRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameDetailRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameUserCredentialVo;
import com.kaihei.esportingplus.user.domain.entity.UserGameRole;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class UserGameRoleAssembler {
    public static List<UserGameBaseRoleInfoVo> convertToBaseInfoVoList(List<UserGameRole> entities) {
        List<UserGameBaseRoleInfoVo> list = new ArrayList<>();
        if (entities != null && !entities.isEmpty()) {
            entities.forEach(u -> list.add(convertToBaseInfoVo(u)));
        }
        return list;
    }

    public static UserGameBaseRoleInfoVo convertToBaseInfoVo(UserGameRole u) {
        if (u != null) {
            UserGameBaseRoleInfoVo vo = new UserGameBaseRoleInfoVo();
            BeanUtils.copyProperties(u, vo);
            return vo;
        }
        return null;
    }

    public static UserGameDetailRoleInfoVo convertToDetailsInfoVoList(UserGameRole ugr,
                                                                      UserGameDetailRoleInfoVo detailRoleInfoVo) {
        if (ugr != null) {
            UserGameDetailRoleInfoVo vo = new UserGameDetailRoleInfoVo();
            BeanUtils.copyProperties(ugr, vo);
            if (detailRoleInfoVo != null && ObjectTools.isNotEmpty(detailRoleInfoVo.getCredentials())) {
                vo.setCredentials(detailRoleInfoVo.getCredentials());
            }
            return vo;
        }
        return null;
    }

    public static UserGameDetailRoleInfoVo convertToDetailsInfoVoList(UserGameRole ugr,
                                                                      List<UserGameUserCredentialVo> userGameUserCredentialVos) {
        if (ugr != null) {
            UserGameDetailRoleInfoVo vo = new UserGameDetailRoleInfoVo();
            BeanUtils.copyProperties(ugr, vo);
            if (userGameUserCredentialVos != null) {
                vo.setCredentials(userGameUserCredentialVos);
            }
            return vo;
        }
        return null;
    }
}
