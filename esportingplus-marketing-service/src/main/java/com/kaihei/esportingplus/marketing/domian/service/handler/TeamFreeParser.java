package com.kaihei.esportingplus.marketing.domian.service.handler;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.marketing.api.enums.TeamRoleType;
import com.kaihei.esportingplus.marketing.api.event.TeamFinishOrderEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamMember;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * TODO 功能描述
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/10 19:15
 */
public interface TeamFreeParser {

    /**
     * 解析车队成员，返回结构为<暴鸡,老板列表>
     *
     * @param teamFreeEvent 车队信息
     */
    static Pair<TeamMember, List<TeamMember>> parseTeamMembers(
            TeamFinishOrderEvent teamFreeEvent) {
        //分别填充暴鸡和老板到对应列表中
        List<TeamMember> baojiMembers = new ArrayList<>();
        List<TeamMember> boosMembers = new ArrayList<>();
        // TODO 解析车队
        teamFreeEvent.getTeamMemberVOS().forEach(member -> {
             if (member.getUserIdentity().toString().equals(TeamRoleType.BOOS.getCode())) {
                boosMembers.add(member);
            } else if (member.getUserIdentity().toString().equals(TeamRoleType.BAOJI.getCode()) || member.getUserIdentity()
                     .toString().equals(TeamRoleType.BAONIANG.getCode())) {
                baojiMembers.add(member);
            }
        });

        //暴鸡和老板数据需正常解析
        if (CollectionUtils.isEmpty(baojiMembers) || CollectionUtils.isEmpty(boosMembers)) {
            throw new BusinessException(BizExceptionEnum.PARAM_VALID_FAIL);
        }

        return MutablePair.of(baojiMembers.get(0), boosMembers);
    }

}
