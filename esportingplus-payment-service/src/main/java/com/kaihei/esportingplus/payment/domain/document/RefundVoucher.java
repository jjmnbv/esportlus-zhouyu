package com.kaihei.esportingplus.payment.domain.document;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 退款凭证文档对象
 *
 * @author haycco
 */
@Document(collection="refund_vouchers")
@TypeAlias("refund_voucher")
public class RefundVoucher extends AbstractVoucher {

    private static final long serialVersionUID = -3611550268195528059L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
