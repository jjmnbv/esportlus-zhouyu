package com.kaihei.esportingplus.core.data.manager;

import java.util.Collection;
import java.util.List;

/**
 * @Author liuyang
 * @Description  白名单
 * @Date 2018/11/15 14:30
 **/
public interface WhiteListManager {

  Collection<String> queryWhiteList();

  List<String> inWhiteList(Collection<String> uids);

}
