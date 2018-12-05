USE esportingplus_payment;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `app_setting`;
CREATE TABLE `app_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(32) NOT NULL COMMENT '已知有接入支付应用APP的ID',
  `app_name` varchar(32) NOT NULL COMMENT '支付应用APP的名称',
  `state` varchar(8) DEFAULT 'OPEN' COMMENT '支付应用状态：OPEN-开启；CLOSE-关闭',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付应用信息表';

DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT '' COMMENT '支付渠道名称',
  `tag` varchar(8) NOT NULL COMMENT '支付渠道标签',
  `state` varchar(8) DEFAULT 'ENABLE' COMMENT '渠道状态：ENABLE-可用；DISABLE-禁用',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付渠道表';

DROP TABLE IF EXISTS `app_setting_pay_channel`;
CREATE TABLE `app_setting_pay_channel` (
  `app_setting_id` bigint(20) NOT NULL,
  `pay_channel_id` bigint(20) NOT NULL,
  CONSTRAINT `fk_app_setting_id` FOREIGN KEY (`app_setting_id`) REFERENCES `app_setting` (`id`),
  CONSTRAINT `fk_pay_channel_id` FOREIGN KEY (`pay_channel_id`) REFERENCES `pay_channel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付应用与支付渠道关系表';

DROP TABLE IF EXISTS `alipay_setting`;
CREATE TABLE `alipay_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alipay_public_key` varchar(2048) NOT NULL COMMENT 'Alipay公钥',
  `app_id` varchar(64) NOT NULL COMMENT '商户app_id',
  `channel_id` bigint(20) NOT NULL COMMENT '支付渠道ID',
  `charset` varchar(16) NOT NULL COMMENT '编码方式',
  `disable_pay_channels` varchar(256) DEFAULT NULL COMMENT '禁用渠道',
  `enable_pay_channels` varchar(256) DEFAULT NULL COMMENT '可用渠道说明',
  `formal_url` varchar(256) DEFAULT '' COMMENT '正式环境URL',
  `format` varchar(40) NOT NULL COMMENT '返回格式',
  `notify_url` varchar(256) NOT NULL COMMENT '回调URL',
  `product_code` varchar(64) NOT NULL COMMENT '销售产品码',
  `request_url` varchar(256) NOT NULL COMMENT '请求URL',
  `rsa_private_key` varchar(2048) NOT NULL COMMENT '商户私钥',
  `sandbox_enable` varchar(16) NOT NULL DEFAULT 'true' COMMENT '是否开启沙盒环境(true 开启,false 禁用)',
  `sandbox_url` varchar(256) NOT NULL COMMENT '沙盒测试环境地址',
  `sign_type` varchar(16) NOT NULL COMMENT '加密方式',
  `timeout_express` varchar(16) NOT NULL DEFAULT '15d' COMMENT '该笔订单允许的最晚付款时间，逾期将关闭交易。默认是15天，交易就会关闭',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付宝设置表';

DROP TABLE IF EXISTS `capay_setting`;
CREATE TABLE `capay_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alipay_url` varchar(128) NOT NULL COMMENT '提现到支付宝时的URL',
  `app_key` varchar(256) NOT NULL COMMENT '签名',
  `bank_card_url` varchar(128) NOT NULL COMMENT '提现到银行卡时的URL',
  `broker_id` varchar(64) NOT NULL COMMENT '代征主体',
  `channel_id` bigint(20) NOT NULL COMMENT '支付渠道ID',
  `dealer_id` varchar(64) NOT NULL COMMENT '商户代码',
  `desc_key` varchar(256) NOT NULL COMMENT '加密密钥',
  `wechat_pay_url` varchar(128) NOT NULL COMMENT '提现到微信时的URL',
  `support_channel` varchar(64) DEFAULT NULL COMMENT '云账户提现支持渠道(wx,zfb)',
  `notify_url` varchar(256) DEFAULT NULL COMMENT '回调URL',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='云账户设置表';

DROP TABLE IF EXISTS `deduct_order`;
CREATE TABLE `deduct_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT '0.00' COMMENT '扣款金额（单位：元，保留两位小数）',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `currency_type` varchar(16) NOT NULL COMMENT '扣款货币类型:001 暴鸡币,002 暴击值',
  `deduct_type` varchar(16) NOT NULL COMMENT '扣款类型',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '描述',
  `order_id` varchar(128) NOT NULL COMMENT '扣款订单ID',
  `order_type` varchar(64) NOT NULL COMMENT '业务订单类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务订单号',
  `state` varchar(4) NOT NULL COMMENT '扣款状态码:0成功,1失败',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币、暴击值扣款订单表';

DROP TABLE IF EXISTS `external_payment_order`;
CREATE TABLE `external_payment_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attach` varchar(512) DEFAULT '' COMMENT '附加数据',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `channel_id` bigint(20) NOT NULL COMMENT '支付渠道ID',
  `channel_name` varchar(16) NOT NULL COMMENT '支付渠道名称',
  `order_id` varchar(128) NOT NULL COMMENT '订单号',
  `order_type` varchar(16) NOT NULL COMMENT '用户ID',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务订单号',
  `paied_time` datetime DEFAULT NULL COMMENT '支付完成时间,格式 yyyyMMddHHmmss',
  `pre_pay_id` varchar(64) DEFAULT NULL COMMENT '预支付订单号（微信使用的）',
  `source_app_id` varchar(16) NOT NULL COMMENT ' 来源应用ID',
  `state` varchar(32) NOT NULL COMMENT '交易状态',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `currency_type` varchar(16) DEFAULT '' COMMENT '货币类型',
  `notify_url` varchar(256) DEFAULT '' COMMENT '支付通知回调URL',
  `total_fee` int(10) NOT NULL DEFAULT '0' COMMENT '支付金额（单位：分）',
  `transaction_id` varchar(128) DEFAULT '' COMMENT '第三方返回的订单号',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  UNIQUE KEY `uk_order_type_and_out_trade_no` (`out_trade_no`,`order_type`),
  KEY `idx_out_trade_no_and_order_type_external_payment_order` (`out_trade_no`,`order_type`),
  KEY `idx_user_id_and_order_id_external_payment_order` (`user_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付订单表';

DROP TABLE IF EXISTS `external_refund_order`;
CREATE TABLE `external_refund_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attach` varchar(512) DEFAULT NULL COMMENT '附加数据',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `channel_id` bigint(20) NOT NULL COMMENT '支付渠道ID 逻辑外键是 pay_channel 表的channelId',
  `channel_name` varchar(16) NOT NULL COMMENT '支付渠道名称',
  `goods_type` varchar(16) DEFAULT NULL COMMENT '商品类型',
  `order_id` varchar(128) NOT NULL COMMENT '退款订单号',
  `order_type` varchar(16) DEFAULT '' COMMENT '业务订单类型',
  `out_refund_no` varchar(64) NOT NULL COMMENT '退款订单号',
  `out_trade_no` varchar(64) DEFAULT '' COMMENT '业务订单号',
  `pay_order_id` varchar(128) DEFAULT '' COMMENT '原支付订单号',
  `refund_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `source_app_id` varchar(16) DEFAULT NULL COMMENT '来源应用ID 逻辑外键是 tapp_settting 表的appId',
  `state` varchar(32) NOT NULL COMMENT '退款状态  REFUNDING退款中 SUCCESS 退款成功 FAILED  退款失败 CANCEL  订单已撤销（用户主动）',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `total_fee` int(10) NOT NULL DEFAULT '0' COMMENT '退款金额（单位为分）',
  `transaction_id` varchar(128) DEFAULT NULL COMMENT '第三方返回的订单号',
  `user_id` varchar(16) NOT NULL COMMENT '用户ID',
  `notify_url` varchar(256) DEFAULT '' COMMENT '支付通知回调URL',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  UNIQUE KEY `uk_out_refund_no` (`out_refund_no`),
  KEY `idx_user_id_and_order_id_external_refund_order` (`order_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方退款订单表';

DROP TABLE IF EXISTS `external_trade_bill_2017_0`;
CREATE TABLE `external_trade_bill_2017_0` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2017_0` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2017_0` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2017年上半年';

DROP TABLE IF EXISTS `external_trade_bill_2017_1`;
CREATE TABLE `external_trade_bill_2017_1` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2017_1` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2017_1` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2017年下半年';

DROP TABLE IF EXISTS `external_trade_bill_2018_0`;
CREATE TABLE `external_trade_bill_2018_0` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2018_0` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2018_0` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2018年上半年';

DROP TABLE IF EXISTS `external_trade_bill_2018_1`;
CREATE TABLE `external_trade_bill_2018_1` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2018_1` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2018_1` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2018年下半年';

DROP TABLE IF EXISTS `external_trade_bill_2019_0`;
CREATE TABLE `external_trade_bill_2019_0` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2019_0` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2019_0` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2019年上半年';

DROP TABLE IF EXISTS `external_trade_bill_2019_1`;
CREATE TABLE `external_trade_bill_2019_1` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2019_1` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2019_1` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2019年下半年';

DROP TABLE IF EXISTS `external_trade_bill_2020_0`;
CREATE TABLE `external_trade_bill_2020_0` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2020_0` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2020_0` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2020年上半年';

DROP TABLE IF EXISTS `external_trade_bill_2020_1`;
CREATE TABLE `external_trade_bill_2020_1` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(16) NOT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单id',
  `order_type` varchar(16) NOT NULL COMMENT '业务单据类型',
  `out_trade_no` varchar(64) NOT NULL COMMENT '业务单号',
  `total_fee` int(10) NOT NULL COMMENT '金额(单位：分)',
  `trade_type` varchar(3) NOT NULL COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)',
  `transaction_id` varchar(128) NOT NULL COMMENT '第三方支付流水号',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `flow_no` (`flow_no`),
  KEY `idx_order_id_external_trade_bill_2020_1` (`order_id`),
  KEY `idx_out_trade_no_and_order_type_external_trade_bill_2020_1` (`out_trade_no`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付流水表-2020年下半年';

DROP TABLE IF EXISTS `external_withdraw_order`;
CREATE TABLE `external_withdraw_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `broker_bank_bill` varchar(64) DEFAULT NULL COMMENT '经纪公司打款交易流水号',
  `broker_wallet_ref` varchar(64) DEFAULT NULL COMMENT '经纪公司打款商户订单号',
  `card_no` varchar(32) NOT NULL COMMENT '用户提现银行卡号、支付宝账号、微信openid',
  `channel` varchar(16) NOT NULL COMMENT '提现渠道',
  `idcard_number` varchar(18) NOT NULL COMMENT '身份证号',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `out_trade_no` varchar(50) NOT NULL COMMENT '业务订单号',
  `paied_time` datetime DEFAULT NULL COMMENT '车队结束时间',
  `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `ref_no` varchar(64) DEFAULT NULL COMMENT '云经纪外部关联流水号',
  `source_app_id` varchar(16) NOT NULL COMMENT '来源应用ID',
  `state` varchar(16) NOT NULL COMMENT '状态(PROCESSING-处理中|SUCCESS-已完成|FAILED-失败|CANCEL-已取消)',
  `status_code` varchar(16) DEFAULT NULL COMMENT '失败状态码',
  `message` varchar(256) DEFAULT NULL COMMENT '状态码详细说明',
  `sys_bank_bill` varchar(128) DEFAULT NULL COMMENT '系统打款交易流水号',
  `sys_wallet_ref` varchar(64) DEFAULT NULL COMMENT '系统打款商户订单号',
  `total_fee` int(10) NOT NULL COMMENT '提现金额(单位:分)',
  `user_id` varchar(32) NOT NULL COMMENT '用户外键id',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_out_trade_no_external_withdraw_order` (`out_trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方支付提现订单表';

DROP TABLE IF EXISTS `gcoin_balance`;
CREATE TABLE `gcoin_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `frozen_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '冻结的暴鸡币数量',
  `gcoin_balance` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '暴鸡币总余额',
  `state` varchar(32) DEFAULT NULL COMMENT '余额账户状态：AVAILABLE表示可用、FROZEN表示冻结、UNAVAILABLE表示不可用',
  `usable_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '可使用的暴鸡币数量',
  `user_id` varchar(128) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_user_id_gcoin_balance` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币余额表';

DROP TABLE IF EXISTS `gcoin_bill`;
CREATE TABLE `gcoin_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '暴鸡币消费额',
  `body` varchar(1024) DEFAULT '' COMMENT '描述',
  `business_type` varchar(16) DEFAULT NULL COMMENT '业务类型',
  `channel` varchar(8) DEFAULT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) DEFAULT NULL COMMENT '流水明细标识',
  `order_dimension` varchar(8) DEFAULT NULL COMMENT '订单维度',
  `order_id` varchar(128) NOT NULL COMMENT '订单ID',
  `order_type` varchar(8) NOT NULL COMMENT '订单类型',
  `source_id` varchar(16) DEFAULT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `subject` varchar(128) NOT NULL DEFAULT '' COMMENT '主题',
  `trade_type` varchar(8) NOT NULL COMMENT '交易类型',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_no` (`flow_no`),
  KEY `idx_user_id_and_order_type_gcoin_bill` (`user_id`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币流水表';

DROP TABLE IF EXISTS `gcoin_payment_order`;
CREATE TABLE `gcoin_payment_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额（单位：元，保留两位小数）',
  `attach` varchar(128) DEFAULT NULL COMMENT '附件数据（json字符串）',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `completed_date` varchar(64) DEFAULT NULL COMMENT '支付完成时间',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '描述',
  `order_id` varchar(128) NOT NULL COMMENT '支付订单号',
  `order_type` varchar(16) NOT NULL COMMENT '业务订单类型',
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '第三方业务订单号',
  `refunded_amount` decimal(19,2) DEFAULT '0.00' COMMENT '已退金额（单位：元，保留两位小数）',
  `refunding_amount` decimal(19,2) DEFAULT '0.00' COMMENT '退款中的金额（单位：元，保留两位小数）',
  `source_id` varchar(16) NOT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `state` varchar(16) NOT NULL COMMENT '支付订单状态码',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  UNIQUE KEY `uk_order_type_and_out_trade_no` (`out_trade_no`,`order_type`),
  KEY `idx_order_type_and_out_trade_no_gcoin_payment_order` (`out_trade_no`,`order_type`),
  KEY `idx_user_id_and_order_id_gcoin_payment_order` (`order_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币支付订单表';

DROP TABLE IF EXISTS `gcoin_recharge_order`;
CREATE TABLE `gcoin_recharge_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '充值金额（单位为元，两位小数）',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `channel` varchar(16) DEFAULT NULL COMMENT '支付渠道',
  `currency_type` varchar(16) DEFAULT NULL COMMENT '货币类型',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '描述',
  `device_id` varchar(128) DEFAULT NULL COMMENT 'apple设备ID',
  `gcoin_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '暴鸡币数量',
  `order_id` varchar(128) NOT NULL COMMENT '充值订单号',
  `order_type` varchar(30) NOT NULL COMMENT '业务订单类型',
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '业务订单号',
  `pay_in_amount` decimal(19,2) DEFAULT '0.00' COMMENT '实收金额',
  `payment_date` varchar(64) DEFAULT NULL COMMENT '第三方支付时间，付款时间',
  `payment_order_no` varchar(128) DEFAULT NULL COMMENT '第三方支付订单号',
  `recharge_type` varchar(16) DEFAULT NULL COMMENT '充值类型',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `source_id` varchar(16) NOT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `state` varchar(16) NOT NULL COMMENT '订单状态',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_user_id_and_order_id_gcoin_recharge_order` (`order_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币充值订单表';

DROP TABLE IF EXISTS `gcoin_refund_order`;
CREATE TABLE `gcoin_refund_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额（单位：元，保留两位小数）',
  `attach` varchar(128) DEFAULT NULL COMMENT '附件数据（json字符串）',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `completed_date` varchar(64) DEFAULT NULL COMMENT '支付完成时间',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '描述',
  `order_id` varchar(128) NOT NULL COMMENT '退款订单号',
  `order_type` varchar(30) NOT NULL COMMENT '业务订单类型',
  `out_refund_no` varchar(64) DEFAULT NULL COMMENT '业务退款订单号',
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '业务订单号',
  `source_id` varchar(16) NOT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `state` varchar(16) NOT NULL COMMENT '退款订单状态码',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  UNIQUE KEY `uk_order_type_and_out_refund_no` (`out_refund_no`,`order_type`),
  KEY `idx_order_type_and_out_refund_no_gcoin_refund_order` (`out_refund_no`,`order_type`),
  KEY `idx_user_id_and_order_id_gcoin_refund_order` (`order_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币退款订单表';

DROP TABLE IF EXISTS `gcoin_reward_order`;
CREATE TABLE `gcoin_reward_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '描述',
  `gcoin_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额（单位为元，两位小数）',
  `order_id` varchar(128) DEFAULT NULL COMMENT '充值订单ID',
  `received_user_id` varchar(64) NOT NULL COMMENT '被打赏收入用户ID',
  `source_id` varchar(16) NOT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `starlight_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '兑换暴击值数量',
  `state` varchar(16) DEFAULT NULL COMMENT '交易状态：001（交易创建，待支付）、002（已支付成功）、003（已退款）',
  `subject` varchar(256) DEFAULT '' COMMENT '主题',
  `user_id` varchar(64) NOT NULL COMMENT '打赏支出用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_user_id_and_order_id_gcoin_reward_order` (`order_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴鸡币打赏订单表';

DROP TABLE IF EXISTS `order_income`;
CREATE TABLE `order_income` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attach` varchar(128) NOT NULL COMMENT '附加信息',
  `baoji_income` int(11) NOT NULL COMMENT '暴鸡、暴娘抽成金额（单位分）',
  `flow_no` varchar(128) NOT NULL COMMENT '流水号',
  `income_ordernum` varchar(128) DEFAULT NULL COMMENT '订单id',
  `order_type` int(11) DEFAULT NULL COMMENT '订单类型(1:游戏订单类型,2:暴鸡认证订单类型,3:充值订单类型,\n4:奖励订单类型,5:违规扣款类型,6:福利金类型,\n7:抽奖奖金,8:车队订单,9:悬赏订单,\n10:系统充值订单类型,11:提现订单类型,12:系统扣款订单类型,\n13:工作室提现订单类型,14:每周好运礼包订单类型,\n16:技能订单类型,18:RPG保证金认证订单,17:DNF小程序订单,\n19:暴鸡币充值订单类型,20:暴鸡值兑换订单类型)',
  `platform_income` int(11) NOT NULL COMMENT '平台抽成',
  `ratio` decimal(4,2) NOT NULL DEFAULT '0.00' COMMENT '抽成比例 暴鸡：平台',
  `total_amount` int(11) NOT NULL COMMENT '总金额（单位分）',
  `user_id` varchar(128) NOT NULL COMMENT '用户id',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_income_ordernum` (`income_ordernum`),
  KEY `idx_income_ordernum_order_income` (`income_ordernum`),
  KEY `idx_user_id_order_income` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单收益分成表';

DROP TABLE IF EXISTS `deduct_ratio_setting`;
CREATE TABLE `deduct_ratio_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(64) DEFAULT '' COMMENT '适用费率标记：CALC_ORDER 订单结算；WITHDRAW 收益提现',
  `description` varchar(128) DEFAULT '' COMMENT '适用费率描述',
  `ratio` float(4,2) DEFAULT '0.00' COMMENT '抽成比例',
  `state` varchar(8) DEFAULT 'ENABLE' COMMENT '状态: ENABLE-有效、DISABLE-禁用',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统扣减费用比率设置表';

DROP TABLE IF EXISTS `starlight_balance`;
CREATE TABLE `starlight_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `balance` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '暴击值数量',
  `frozen_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '冻结的暴击值数量',
  `state` varchar(16) NOT NULL COMMENT '状态码:AVAILABLE表示可用、FROZEN表示冻结、UNAVAILABLE表示不可用',
  `usable_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '可使用的暴击值数量',
  `user_id` varchar(128) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_user_id_starlight_balance` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴击值余额表';

DROP TABLE IF EXISTS `starlight_bill`;
CREATE TABLE `starlight_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '暴鸡币消费额',
  `body` varchar(1024) DEFAULT '' COMMENT '描述',
  `business_type` varchar(16) DEFAULT NULL COMMENT '业务类型',
  `channel` varchar(8) DEFAULT NULL COMMENT '支付渠道',
  `flow_no` varchar(128) DEFAULT NULL COMMENT '流水明细标识',
  `order_dimension` varchar(8) DEFAULT NULL COMMENT '订单维度',
  `order_id` varchar(128) NOT NULL COMMENT '订单ID',
  `order_type` varchar(8) NOT NULL COMMENT '订单类型',
  `source_id` varchar(16) DEFAULT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `subject` varchar(128) NOT NULL DEFAULT '' COMMENT '主题',
  `trade_type` varchar(8) NOT NULL COMMENT '交易类型',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_no` (`flow_no`),
  KEY `idx_user_id_and_order_type_starlight_bill` (`user_id`,`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暴击值流水表';

DROP TABLE IF EXISTS `tenpay_setting`;
CREATE TABLE `tenpay_setting`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '最近更新时间',
  `api_caertificate_path` varchar(64) NOT NULL DEFAULT '' COMMENT '微信支付证书位置cret.12',
  `api_secret` varchar(128) NOT NULL DEFAULT '' COMMENT 'api秘钥',
  `app_id` varchar(64) NOT NULL DEFAULT '' COMMENT '应用id',
  `channel_id` bigint(10) NOT NULL DEFAULT 0 COMMENT '渠道id',
  `mch_id` varchar(64)  NOT NULL DEFAULT '' COMMENT '商户id',
  `notify_url` varchar(256) NOT NULL DEFAULT '' COMMENT '支付回调地址',
  `sandbox_enable` varchar(16) NOT NULL DEFAULT 'true' COMMENT '是否开启沙盒环境(true 开启,false 禁用)',
  `sign_type` varchar(16) NOT NULL DEFAULT 'MD5' COMMENT '签名方法',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_channel_id_tenpay_setting`(`channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '腾讯支付设置表';


DROP TABLE IF EXISTS `withdraw_order`;
CREATE TABLE `withdraw_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '提现暴击值',
  `body` varchar(1024) DEFAULT '' COMMENT '内容',
  `channel` varchar(10) NOT NULL COMMENT '提现渠道',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '描述',
  `fee` decimal(19,2) DEFAULT '0.00' COMMENT '手续费',
  `is_notify` varchar(1) NOT NULL COMMENT '提现是否需要通知(1-需要 0-不需)',
  `money_type` varchar(20) NOT NULL COMMENT '货币类型(starlight默认)',
  `order_id` varchar(128) NOT NULL COMMENT '订单id',
  `order_type` varchar(32) NOT NULL COMMENT '订单类型(001|002)',
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '第三方订单号',
  `source_id` varchar(16) NOT NULL COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)',
  `star_amount` decimal(19,2) NOT NULL DEFAULT '0.00' COMMENT '提现暴击值',
  `state` varchar(10) NOT NULL COMMENT '订单状态',
  `subject` varchar(128) DEFAULT '' COMMENT '主题',
  `user_id` varchar(128) NOT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_out_trade_no_withdraw_order` (`out_trade_no`),
  KEY `idx_order_id_and_user_id_withdraw_order` (`order_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='提现兑换订单表';

DROP TABLE IF EXISTS `withdraw_tax_record`;
CREATE TABLE `withdraw_tax_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` varchar(10) NOT NULL COMMENT '用户id',
  `total_fee` int(10) NOT NULL COMMENT '税前金额',
  `tax_fee` int(10) NOT NULL COMMENT '交税金额',
  `income_fee` int(10) NOT NULL COMMENT '税后金额',
  `state` varchar(16) NOT NULL COMMENT '扣款状态(PROCESSING-处理中|SUCCESS-已完成|FAILED-失败|CANCEL-已取消)',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='提现扣税记录';

DROP TABLE IF EXISTS `withdraw_config`;
CREATE TABLE `withdraw_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `state` varchar(8) DEFAULT 'ENABLE' COMMENT '状态: ENABLE-有效、DISABLE-禁用',
  `withdraw_limit` int(4) NOT NULL COMMENT '每日提现次数',
  `withdraw_min` int(10) NOT NULL COMMENT '单次提现最小金额(单位:分)',
  `withdraw_max` int(10) NOT NULL COMMENT '单次提现最大额度(单位:分)',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='提现配置表';

DROP TABLE IF EXISTS `withdraw_audit_record`;
CREATE TABLE `withdraw_audit_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `uid` varchar(10) NOT NULL COMMENT '用户id',
  `total_fee` int(10) NOT NULL COMMENT '提现总金额',
  `channel` varchar(32) NOT NULL COMMENT '提现渠道',
  `verify_state` varchar(32) NOT NULL COMMENT '审批状态',
  `block_state` varchar(32) NOT NULL COMMENT '截停状态',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `source_app_id` varchar(16) NOT NULL COMMENT '来源应用ID',
  `client_ip` varchar(32) DEFAULT NULL COMMENT '客户端ip',
  `finish_date` datetime DEFAULT NULL COMMENT '提现完成时间',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='提现审批记录表';

SET FOREIGN_KEY_CHECKS=1;
