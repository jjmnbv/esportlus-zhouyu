package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import java.util.Map;

/**
 * 终端用户ES管理
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/25 10:47
 */
public interface MembersUserESManager {

    /**
     * 根据用户uid查询用户信息
     *
     * @param uid 用户uid
     */
    public Map<String, Object> getMembersUserES(String uid);

    /**
     * 保存用户信息到es
     *
     * @param membersUser 用户信息
     */
    public void saveMembersUserES(MembersUser membersUser);

}
