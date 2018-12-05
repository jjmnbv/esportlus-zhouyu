package com.kaihei.esportingplus.payment.util;

import com.kaihei.esportingplus.payment.api.params.PageParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * @author: tangtao
 **/
public class PageUtils {

    public static final Sort DEFAULT_SORT = new Sort(new Order(Sort.Direction.DESC, "createDate"));
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 999;

    public static PageRequest getPageRequest(PageParams params) {
        return PageUtils.getPageRequest(params, DEFAULT_SORT);
    }

    public static PageRequest getPageRequest(PageParams params, Sort sort) {
        if (StringUtils.isNotBlank(params.getPage())
                && StringUtils.isNotBlank(params.getSize())) {
            int page = Integer.parseInt(params.getPage());
            int size = Integer.parseInt(params.getSize());
            page = page > 1 ? page - 1 : DEFAULT_PAGE;
            size = size > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : size < 1 ? DEFAULT_PAGE_SIZE : size;
            return new PageRequest(page, size, sort);
        }
        return new PageRequest(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, sort);
    }
}
