package com.kaihei.esportingplus.payment.migrate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: tangtao
 **/
@Entity
@Table(name = "payment_alipayorder")
public class AliPayOrder extends PayOrder {
    /**
     * CREATE TABLE `payment_alipayorder` (
     *   `out_trade_no` varchar(50) NOT NULL,
     *   `total_fee` int(10) unsigned NOT NULL,
     *   `attach` varchar(127) NOT NULL,
     *   `prepay_id` varchar(64) NOT NULL,
     *   `transaction_id` varchar(32) NOT NULL,
     *   `status` smallint(5) unsigned NOT NULL,
     *   `pay_time` datetime(6) DEFAULT NULL,
     *   `create_time` datetime(6) NOT NULL,
     *   `update_time` datetime(6) NOT NULL,
     *   `raw_data` longtext NOT NULL,
     *   `user_id` int(11) NOT NULL,
     *   `query_data` longtext NOT NULL,
     *   `query_status` varchar(50) NOT NULL,
     *   `out_request_no` varchar(50) NOT NULL,
     *   `refund_query_data` longtext NOT NULL,
     *   `refund_query_status` varchar(50) NOT NULL,
     *   `package_name` varchar(20) NOT NULL,
     *   PRIMARY KEY (`out_trade_no`),
     *   KEY `payment_alipayorder_user_id_110b676e_fk_members_user_id` (`user_id`),
     *   CONSTRAINT `payment_alipayorder_user_id_110b676e_fk_members_user_id` FOREIGN KEY (`user_id`) REFERENCES `members_user` (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
     */

}
