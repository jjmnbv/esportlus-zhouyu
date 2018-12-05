package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.core.utils.RonYunUtils;

import java.util.List;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/19 10:39
 **/
public class ReceiverBuilder {

    public static UserReceiver multiUser(List<String> uids){
        return () -> RonYunUtils.encodeIMUser(uids);
    }

    public static GroupReceiver group(List<String> groups){
        return () -> groups;
    }

    public static GroupDirectReceiver groupDirect(List<String> groups, List<String> toUsers){
        return new GroupDirectReceiver() {
            @Override
            public List<String> toUsers() {
                return groups;
            }

            @Override
            public List<String> getGroups() {
                return RonYunUtils.encodeIMUser(toUsers);
            }
        };
    }

}
