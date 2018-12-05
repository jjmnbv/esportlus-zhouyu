package com.kaihei.esportingplus.api.vo;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * 评价列表信息对象
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@Builder
public class EvaluateListVo implements Serializable {
    private static final long serialVersionUID = 2348852389701399382L;
    /**
     * 评价信息列表
     */
    private List<EvaluateContentVo> evaluateList;

    /**
     * 评价信息条数
     */
    private int evaluateCount;

    public List<EvaluateContentVo> getEvaluateList() {
        return evaluateList;
    }

    public void setEvaluateList(List<EvaluateContentVo> evaluateList) {
        this.evaluateList = evaluateList;
    }

    public int getEvaluateCount() {
        return evaluateCount;
    }

    public void setEvaluateCount(int evaluateCount) {
        this.evaluateCount = evaluateCount;
    }
}
