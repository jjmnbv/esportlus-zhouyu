package com.kaihei.esportingplus.payment.domain.document;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 体现凭证文档对象
 *
 * @author haycco
 */
@Document(collection="withdraw_vouchers")
@TypeAlias("withdraw_voucher")
public class WithdrawVoucher extends AbstractVoucher {

    private static final long serialVersionUID = -7913783919436805566L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
