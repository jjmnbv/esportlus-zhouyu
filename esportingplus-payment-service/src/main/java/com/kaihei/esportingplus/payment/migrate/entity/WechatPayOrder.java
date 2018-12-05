package com.kaihei.esportingplus.payment.migrate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: tangtao
 **/
@Entity
@Table(name = "payment_wechatpayorder")
public class WechatPayOrder extends PayOrder {

}
