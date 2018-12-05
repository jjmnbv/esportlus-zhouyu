package com.kaihei.esportingplus.core.message;

import java.util.List;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/19 10:23
 **/
public interface Receiver {
}


interface UserReceiver extends Receiver {
    List<String> getUsers();
}

interface GroupReceiver extends Receiver {
    List<String> getGroups();
}

interface GroupDirectReceiver extends GroupReceiver {
    List<String> toUsers();
}