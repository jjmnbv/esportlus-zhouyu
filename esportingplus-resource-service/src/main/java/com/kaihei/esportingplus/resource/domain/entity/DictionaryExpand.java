package com.kaihei.esportingplus.resource.domain.entity;

import lombok.Data;

@Data
public class DictionaryExpand implements DictEntity {

    /**
     * 附加属性Id
     */
    private Integer id;

    /**
     * 字典Id
     */
    private Integer dictionaryId;

    /**
     * 属性值Id
     */
    private Integer propertyValueId;
    /**
     * 附加属性名
     */
    private String name;

    /**
     * 排序
     */
    private Integer orderIndex;

    /**
     * 备注
     */
    private String remark;

    /**
     * 值
     */
    private String value;

    /**
     * 状态
     */
    private Integer status;
}
