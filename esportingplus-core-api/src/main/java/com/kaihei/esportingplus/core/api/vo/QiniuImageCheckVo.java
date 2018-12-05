package com.kaihei.esportingplus.core.api.vo;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description: 图片审核结果VO
 * @date: 2018/10/31 16:56
 */
public class QiniuImageCheckVo implements Serializable {

    private static final long serialVersionUID = 6940113201332233133L;


    /**
     * 审核是否通过
     */
    private Boolean verify;

    /**
     * 验证结果
     *      (0, '正常')
     *      (1, '性感')
     *      (2, '色情')
     *      (3, '暴恐')
     */
    private Integer verifyCode;

    /**
     * 是否需要人工复审该图片。
     * true需要false不需要
     */
    private String review;

    /**
     * 表示该图像被识别为某个分类的概率值
     */
    private float score;

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public Integer getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(Integer verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
