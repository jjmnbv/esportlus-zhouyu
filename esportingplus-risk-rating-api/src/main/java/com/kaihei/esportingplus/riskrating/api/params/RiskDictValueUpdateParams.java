package com.kaihei.esportingplus.riskrating.api.params;


import com.kaihei.esportingplus.common.data.Castable;

public class RiskDictValueUpdateParams implements Castable {

    private Long id;
    private Integer dictStatus;

    public Long getId() {
        return id;
    }
    private String itemValue;

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDictStatus() {
        return dictStatus;
    }

    public void setDictStatus(Integer dictStatus) {
        this.dictStatus = dictStatus;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Override
    public String toString() {
        return "RiskDictValueUpdateParams{" +
                "id=" + id +
                ", itemValue='" + itemValue + '\'' +
                '}';
    }
}
