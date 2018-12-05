package com.kaihei.esportingplus.riskrating.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 分页工具类
 * @author chenzhenjun
 */
public class PageRequestUtil {

    public static final int MAX_PAGE_SIZE = 9999;

    public static  PageRequest getPageRequest(String pageIndex, String pageSize) {
        //排序参数
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"));
        PageRequest pageRequest = null;
        if (StringUtils.isNotEmpty(pageIndex) && StringUtils
                .isNotEmpty(pageSize)) {
            int page = Integer.parseInt(pageIndex);
            int size = Integer.parseInt(pageSize);
            if (size == -1) {
                pageRequest = new PageRequest(page - 1, MAX_PAGE_SIZE, sort);
            } else {
                pageRequest = new PageRequest(page - 1, size, sort);
            }
        } else {
            pageRequest = new PageRequest(0, 20, sort);

        }
        return pageRequest;
    }

}
