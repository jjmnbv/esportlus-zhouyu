USE esportingplus_payment;

INSERT INTO esportingplus_payment.pay_channel (id, create_date,last_modified_date,name,state,tag) VALUES
(1, '2018-11-01 21:21:00.000',NULL,'暴鸡币钱包支付','ENABLE','C001'),
(2, '2018-11-01 21:21:00.000',NULL,'QQ支付','ENABLE','C002'),
(3, '2018-11-01 21:21:00.000',NULL,'微信支付','ENABLE','C003'),
(4, '2018-11-01 21:21:00.000',NULL,'支付宝支付','ENABLE','C004'),
(5, '2018-11-01 21:21:00.000',NULL,'苹果支付','ENABLE','C005'),
(6, '2018-11-01 21:21:00.000',NULL,'平台系统支付','ENABLE','C006'),
(7, '2018-11-01 21:21:00.000',NULL,'云账户支付','ENABLE','C007'),
(8, '2018-11-01 21:21:00.000',NULL,'微信公众号支付','ENABLE','C008'),
(9, '2018-11-01 21:21:00.000',NULL,'微信支付-小程序','ENABLE','C009'),
(10,'2018-11-01 21:21:00.000',null,'qq小程序支付','ENABLE','C010'),
(11,'2018-11-01 21:21:00.000',null,'qq公众号支付','ENABLE','C011')
;

INSERT INTO esportingplus_payment.app_setting(id, create_date, last_modified_date, app_id, app_name, state) VALUES
(1, '2018-11-01 21:21:00.000',NULL,'IOS_BJDJ','AppStore应用-暴鸡电竞','OPEN'),
(2, '2018-11-01 21:21:00.000',NULL,'IOS_BJ','AppStore应用-暴鸡','OPEN'),
(3, '2018-11-01 21:21:00.000',NULL,'ANDROID_BJDJ','Android应用-暴鸡电竞','OPEN'),
(4, '2018-11-01 21:21:00.000',NULL,'WECHAT_PA_BJDJ','微信公众号应用-暴鸡电竞','OPEN'),
(5, '2018-11-01 21:21:00.000',NULL,'WECHAT_MP_BJDJ','微信小程序应用-暴鸡电竞','OPEN'),
(6, '2018-11-01 21:21:00.000',NULL,'H5_BJDJ','H5-暴鸡电竞','OPEN'),
(7, '2018-11-01 21:21:00.000',NULL,'H5_BJ','H5_暴鸡','OPEN')
;

INSERT INTO esportingplus_payment.app_setting_pay_channel(app_setting_id, pay_channel_id) VALUES
(1, 1),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(2, 1),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 6),
(3, 7),
(4, 8),
(5, 9);

INSERT INTO esportingplus_payment.alipay_setting(id, create_date,  alipay_public_key, app_id, channel_id, charset,  formal_url, format, notify_url, product_code, request_url, rsa_private_key, sandbox_enable, sandbox_url, sign_type, timeout_express)
VALUES (1, '2018-11-14 19:41:31', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtEBoWz0q0lPXqotBSoTB0kUwadM267ybXNYPzGgEgv1ZxVe8RsnuQZ7IVT2sdiL1OftdZ0ZEf3GbHgszQrE4ZubblCGxA4eCK7JGmjn5TRD71Q5vo2+TXi9+d+mlrl7l6XUj+ZOr5b8gKKBrXPTbCk2XyMtidklBMse73bOZT7jx+2qWjVknLtY7OyPCxHIpGsBii0SxAasNi4gkmCNQf00Cql1tyDg/ZodZZOV0+3y7mviQgX2kB0b3IeI3EbJbhs4mFp6y4S/XU1mnfJ35soPvNvvmWh4MDwHAMMJOWvQEW5ugS5NnvdEXSIDcTS4zuRsVpHqjxKAKrYH8aVPshQIDAQAB',
'2016091600527918', 4, 'utf-8',  'https://openapi.alipaydev.com/gateway.do', 'json', 'http://kvhy6u.natappfree.cc/v3/generator/payment/trade/paymentNotify/baoji/C004', 'QUICK_MSECURITY_PAY', 'https://openapi.alipaydev.com/gateway.do', 'MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC510ruDM08wy+LRYeweD3WqIce4Am6CiJkk+NWh8Uk0139TtjspFXVrK+whSza6GKDQztZL9NkM+zhQdhGMc9k03jjSgrw8iID83fRQj+9IHPu7AbyEkxF0egphw67+7lKPHC6lMf5kvMotkGnoXeDyNpBxTqL7h5qUYDky2Avb/UD6o7I3gnr/kCSxWFEJJXTKcexARoGzwfYoHo88SG3hdEXp5h2xfZmGPMqnkk1GpUrQYu5SLW7eoE2Ca9eqAa/UBgwLXcEVFGMPcuZI4aDM0B6cb1M/Wl8ET4tatuhquato1W7Diuxmu1aoU0kBxuidI5FemH6v5OT1l7vpr4vAgMBAAECggEBALBtNERkQN+jiVpViui8bjCAU667EGDeyCHlyBhY0o8QXpVhKk0WAirVQwF4nMnBfeyRgiE8ZxoktYhv4ytYlERrAsP8ggCWTjakB6bQlwQscANtbXaYanKKQTZTcmiCJDjWwkpPfr1vhYqr5A6JGpYwswtnexpm19clQxFyBgFhUPo+TKKNC0exD6jO3ggXSqiwN/KuwvlE6yUtQUYtpR+SrnHgOhR3Gj4dzNj7sdGiAtxTjg74n0hw1YWlJS0Ae/WGpbU7E2xRWctMMHRSng9jHWuU5pM/nUJ9J9T18i0fag4o6uzpUyvgUCXQMAVzthBEPEMG754kUogjjhFVEOkCgYEA240o6TZX/YbJtZbsBWgkvieFs4unZR4UzxGKa91kxC/duP43Y+9xFgG2xR1GAz3TkdIeqRrieDcYCWOht8YzLrBm7Xus+MYR4gBhgu3zcpPvKRTgX/ZBqj5miu9GAuenKbWvSE5lL1T7YYQkok84odwvLT8mwcM4hWOQNmU829MCgYEA2LF0YfpSFNGuleV1jQZ4In2yjSUv4PXP+WEJnJTSHYajEfIZg6NHAd85xCFMbZe0FOmDShl8WvtTZ5AiFLXGSgVpaBRVI4F4BSXEsQvrGkzJZeOB072l04ioieYBXZVqqSqmWfURO9Zfh9zSd13wqG0gVTK8LQxNuTcmNRhKJrUCgYAiJnjgnbVeGCMz/b/Er4hXk6o2XViyX9V4yA+x64QLCC8C4XgYMVxzwdx+GKmVQ36vRfHC+A2WCWvRug+jmE7JymKYOg6Tu6t4bfq89OpTPjOLPlYQVyuroX0tyz4moYXc9QfIxXjzvdJEPtu763qxoqAr95C9vvIBvZTnASRQRQKBgQCTWqj/749dpPEJ8c2sJtgO7Fc88CrM2Zuhw8Ebf4Aan/HIRE/hWmjw9vF7VAF7DQxipCCphQcxf3LKHBNw7eN3dpKlOh5jVKlfJr3If4hSjCDwbnNcn7xiSq4BiC0pjG/Jtb0Fs0cS38Z6x0k/7tu2TO8NwMCCCbnY143d0TSreQKBgQCNZeteW8O5JAJ9FF/bThaptjnnrf7Dignak3crdzhMK3HnSRKN1+KaWybXAhQVvwoX1vs9fWHOU1tFOxcr3ZttWm66KmozmnA/uXDw3uRPSx/YGa5JQWSfP/PnUwVvQ4y02/zbECGjqepuq7axeZdxCexlf3ppe6/HdW6NxUVVrg==', 'true', 'https://openapi.alipaydev.com/gateway.do', 'RSA2', '15d');

INSERT INTO esportingplus_payment.tenpay_setting(id, create_date, last_modified_date, api_caertificate_path, api_secret, app_id, channel_id, mch_id, notify_url, sandbox_enable, sign_type)
 VALUES
(1, '2018-11-01 21:21:00.000',NULL, '/home/java_cert/wechat/apiclient_cert.p12', 'Z02uEcvCJDW34b1mgpR9VtioGknMxOsd', 'wxa6c20cfbf5514cba', 3, '1434225402', 'https://dev.kaiheikeji.com/v3', 'true', 'MD5'),
(2, '2018-11-01 21:21:00.000',NULL, '/home/java_cert/qqpay/apiclient_cert.p12', '5u32UF90Q1tSHYRMslbj6eDhaIkpmVcO', '1105884111', 2, '1418215101', 'https://dev.kaiheikeji.com/v3', 'true', 'MD5'),
(3, '2018-11-01 21:21:00.000',NULL, '/home/java_cert/weimob/apiclient_cert.p12', 'Osd4b1mgpR7VtioGknZ02uEcvCJDW3Mx', 'wx67ed7f6a3fadef81', 9, '1422913202', 'https://dev.kaiheikeji.com/v3', 'true', 'MD5'),
(4, '2018-11-01 21:21:00.000',NULL, '/home/java_cert/wechat/apiclient_cert.p12', 'Z02uEcvCJDW34b1mgpR9VtioGknMxOsd', 'wxa6c20cfbf5514cba', 8, '1422913202', 'https://dev.kaiheikeji.com/v3', 'true', 'MD5');

INSERT INTO esportingplus_payment.deduct_ratio_setting(id, create_date, last_modified_date, tag, ratio, state, description)
 VALUES
(1, '2018-11-01 21:21:00.000',NULL, 'CALC_ORDER', 0.10, 'ENABLE', '订单结算抽成比例'),
(2, '2018-11-01 21:21:00.000',NULL, 'WITHDRAW', 0.10, 'ENABLE', '提现税费抽成比例');

INSERT INTO `esportingplus_payment`.`withdraw_config` (`id`, `withdraw_limit`, `withdraw_min`, `withdraw_max`, `create_date`, `last_modified_date`)
 VALUES
('1', '1', '200', '50000', '2018-11-26 16:55:48', NULL);