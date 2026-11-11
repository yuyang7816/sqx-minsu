/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80409
 Source Host           : localhost:3306
 Source Schema         : jdms

 Target Server Type    : MySQL
 Target Server Version : 80409
 File Encoding         : 65001

 Date: 05/11/2026 16:34:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'app升级配置id',
  `android_wgt_url` varchar(600) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '安卓升级地址',
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `des` varchar(600) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `ios_version` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '苹果版本',
  `ios_wgt_url` varchar(600) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '苹果升级地址',
  `method` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '是否强制升级',
  `version` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '安卓版本',
  `wgt_url` varchar(600) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '通用升级地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = big5 COLLATE = big5_chinese_ci COMMENT = 'app升级配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES (5, 'https://www.pgyer.com/hb2v', '2020-07-20 18:35:00', '升级', '', '', 'false', '1.0.0', 'https://www.pgyer.com/hb2v');
INSERT INTO `app` VALUES (6, 'https://www.pgyer.com/hb2v', '2020-07-20 18:35:00', '升级', NULL, '', 'false', '1.0.0', 'https://www.pgyer.com/hb2v');
INSERT INTO `app` VALUES (7, 'https://www.pgyer.com/hb2v', '2020-07-20 18:35:00', '升级', NULL, '', 'false', '1.0.0', 'https://www.pgyer.com/hb2v');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'bannerId',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `state` int(0) NULL DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(0) NULL DEFAULT NULL COMMENT '分类1banner图2金刚区分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(0) NULL DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'banner图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '2026-07-21 13:24:37', '轮播图', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/07/21/d31ee76231c819c511151bee7a9b671f.png', 1, 1, NULL, 1, NULL);

-- ----------------------------
-- Table structure for cancel_account
-- ----------------------------
DROP TABLE IF EXISTS `cancel_account`;
CREATE TABLE `cancel_account`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `status` int(0) NOT NULL COMMENT '状态: 1申请中 2已注销 3撤销申请',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户注销申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cancel_account
-- ----------------------------
INSERT INTO `cancel_account` VALUES (3, 30, '花花', '花花', '我要注销账号！', 2, '2026-01-23 14:11:53', '2026-07-02 16:53:17');

-- ----------------------------
-- Table structure for cash_out
-- ----------------------------
DROP TABLE IF EXISTS `cash_out`;
CREATE TABLE `cash_out`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请时间',
  `is_out` int(0) NULL DEFAULT NULL COMMENT '是否转账',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员编号',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `state` int(0) NULL DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `classify` int(0) NULL DEFAULT NULL COMMENT '提现方式 1支付宝 2微信小程序  3微信公众号',
  `rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手续费',
  `wx_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信收款二维码',
  `user_type` int(0) NULL DEFAULT NULL COMMENT '1用户 2商户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cash_out
-- ----------------------------

-- ----------------------------
-- Table structure for common_info
-- ----------------------------
DROP TABLE IF EXISTS `common_info`;
CREATE TABLE `common_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `max` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '暂未使用',
  `min` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置名称',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '对应值',
  `condition_from` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 517 CHARACTER SET = big5 COLLATE = big5_chinese_ci COMMENT = '通用配置管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of common_info
-- ----------------------------
INSERT INTO `common_info` VALUES (1, '2022-01-09 13:14:16', NULL, '客服二维码', 1, '', 'image');
INSERT INTO `common_info` VALUES (2, '2020-10-22 14:39:31', NULL, '公众号二维码', 2, '', 'image');
INSERT INTO `common_info` VALUES (5, '2020-07-09 17:59:45', NULL, '微信公众号APPID', 5, '', 'weixin');
INSERT INTO `common_info` VALUES (11, '2020-08-10 15:06:55', NULL, '单笔最高可提现金额', 11, '', 'fuwufei');
INSERT INTO `common_info` VALUES (12, '2026-07-31 11:06:53', NULL, '后台服务名称	', 12, '', 'xitong');
INSERT INTO `common_info` VALUES (19, '2023-07-21 15:59:32', NULL, 'h5域名配置', 19, '', 'xitong');
INSERT INTO `common_info` VALUES (20, '2021-08-27 10:49:01', NULL, '后台管理平台域名配置', 20, '', 'xitong');
INSERT INTO `common_info` VALUES (21, '2020-02-25 20:43:38', NULL, '微信公众号秘钥', 21, '', 'weixins');
INSERT INTO `common_info` VALUES (22, '2023-07-14 13:41:23', NULL, '师傅端域名配置', 22, '', 'xitongs');
INSERT INTO `common_info` VALUES (31, '2020-03-28 00:46:38', NULL, '腾讯云短信clientId	', 31, '', 'duanxin');
INSERT INTO `common_info` VALUES (32, '2020-03-28 00:47:05', NULL, '腾讯云短信clientSecret	', 32, '', 'duanxin');
INSERT INTO `common_info` VALUES (44, '2022-07-08 13:52:58', NULL, '联系客服（微信）', 44, '', 'xitong');
INSERT INTO `common_info` VALUES (45, '2022-11-08 16:52:44', NULL, '用户端微信小程序APPID', 45, '', 'weixin');
INSERT INTO `common_info` VALUES (46, '2022-11-08 16:53:03', NULL, '用户端微信小程序秘钥', 46, '', 'weixin');
INSERT INTO `common_info` VALUES (49, '2022-03-14 17:43:13', NULL, '分享安卓下载地址', 49, '', 'xitong');
INSERT INTO `common_info` VALUES (50, '2020-03-29 00:21:40', NULL, '分享苹果下载地址', 50, '', 'xitong');
INSERT INTO `common_info` VALUES (53, '2020-03-29 00:21:40', NULL, '是否显示微信登录', 53, '', 'kaiguan');
INSERT INTO `common_info` VALUES (60, '2020-09-23 15:53:12', NULL, 'APP用户端消息推送PushAppKey', 60, '', 'appxiaoxi');
INSERT INTO `common_info` VALUES (61, '2020-09-23 15:53:24', NULL, 'APP用户端消息推送PushAppId', 61, '', 'appxiaoxi');
INSERT INTO `common_info` VALUES (62, '2020-09-23 15:53:30', NULL, 'APP用户端消息推送PushMasterSecret', 62, '', 'appxiaoxi');
INSERT INTO `common_info` VALUES (63, '2020-06-04 16:34:00', NULL, '企业支付宝APPID', 63, '', 'zhifubao');
INSERT INTO `common_info` VALUES (64, '2020-06-04 16:34:00', NULL, '企业支付宝公钥', 64, '', 'zhifubaos');
INSERT INTO `common_info` VALUES (65, '2020-06-04 16:34:00', NULL, '企业支付宝商户秘钥', 65, '', 'zhifubaos');
INSERT INTO `common_info` VALUES (68, '2020-07-13 15:17:00', NULL, '文件上传阿里云Endpoint', 68, '', 'xitongs');
INSERT INTO `common_info` VALUES (69, '2020-10-22 14:39:49', NULL, '文件上传阿里云账号accessKeyId', 69, '', 'xitongs');
INSERT INTO `common_info` VALUES (70, '2020-10-22 14:39:53', NULL, '文件上传阿里云账号accessKeySecret', 70, '', 'xitongs');
INSERT INTO `common_info` VALUES (71, '2020-07-13 15:01:00', NULL, '文件上传阿里云Bucket名称', 71, '', 'xitongs');
INSERT INTO `common_info` VALUES (72, '2020-07-13 15:01:00', NULL, '文件上传阿里云Bucket域名', 72, '', 'xitongs');
INSERT INTO `common_info` VALUES (74, '2020-07-27 15:01:00', NULL, '微信AppappId', 74, '', 'weixin');
INSERT INTO `common_info` VALUES (75, '2020-07-27 15:01:00', NULL, '微信商户key', 75, '', 'weixins');
INSERT INTO `common_info` VALUES (76, '2020-07-27 15:01:00', NULL, '微信商户号mchId', 76, '', 'weixins');
INSERT INTO `common_info` VALUES (79, '2022-09-10 12:00:14', NULL, '短信服务商（1 腾讯云  2阿里云  3短信宝）', 79, '', 'duanxin');
INSERT INTO `common_info` VALUES (87, '2022-01-09 14:02:19', NULL, '提现最低额度', 87, '', 'fuwufei');
INSERT INTO `common_info` VALUES (90, '2020-10-22 14:38:53', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 90, '', 'duanxin');
INSERT INTO `common_info` VALUES (91, '2020-10-22 14:38:59', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 91, '', 'duanxin');
INSERT INTO `common_info` VALUES (92, '2020-10-22 14:39:05', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 92, '', 'duanxin');
INSERT INTO `common_info` VALUES (93, '2020-10-22 14:39:10', NULL, '阿里云短信accessKeyId', 93, '', 'duanxin');
INSERT INTO `common_info` VALUES (94, '2020-10-22 14:39:15', NULL, '阿里云短信accessSecret', 94, '', 'duanxin');
INSERT INTO `common_info` VALUES (98, '2023-07-24 18:05:11', NULL, '支付宝转账方式 1密钥  2证书  3手动转账', 98, '', 'xitong');
INSERT INTO `common_info` VALUES (105, '2025-01-03 15:33:33', NULL, '是否微信开启小程序跳转开关', 105, '', 'kaiguan');
INSERT INTO `common_info` VALUES (114, '2021-08-30 16:15:16', NULL, '提现手续费', 114, '', 'fuwufei');
INSERT INTO `common_info` VALUES (128, '2023-07-20 17:19:05', NULL, '用户端腾讯地图key', 128, '', 'weixin');
INSERT INTO `common_info` VALUES (129, '2022-01-01 19:00:49', NULL, '酒店默认分成比例', 129, '', 'fuwufei');
INSERT INTO `common_info` VALUES (164, '2022-09-13 15:48:56', NULL, '短信宝用户名', 164, '', 'duanxin');
INSERT INTO `common_info` VALUES (165, '2022-09-13 15:49:05', NULL, '短信宝密码', 165, '', 'duanxin');
INSERT INTO `common_info` VALUES (166, '2023-07-12 09:42:00', NULL, '一级佣金', 166, '', 'fuwufei');
INSERT INTO `common_info` VALUES (167, '2023-07-14 15:09:03', NULL, '二级佣金', 167, '', 'fuwufei');
INSERT INTO `common_info` VALUES (172, '2020-11-04 10:31:40', NULL, '比列(充值的钱比例默认1:1)', 154, '', 'fuwufeis');
INSERT INTO `common_info` VALUES (201, '2023-08-22 14:32:29', NULL, '微信证书地址', 201, '', 'xitong');
INSERT INTO `common_info` VALUES (234, '2020-11-04 10:31:40', NULL, '上传方式 1阿里云oss  2本地', 234, '', 'xitong');
INSERT INTO `common_info` VALUES (245, '2020-11-04 10:31:40', NULL, '用户协议', 245, '', 'xieyi');
INSERT INTO `common_info` VALUES (246, '2020-11-04 10:30:40', NULL, '隐私政策', 246, '', 'xieyi');
INSERT INTO `common_info` VALUES (248, '2022-01-01 17:29:51', NULL, '师傅端微信小程序APPID', 248, '', 'weixin');
INSERT INTO `common_info` VALUES (249, '2022-01-01 17:29:59', NULL, '师傅端微信小程序秘钥', 249, '', 'weixin');
INSERT INTO `common_info` VALUES (250, '2020-07-27 15:01:00', NULL, '师傅端微信商户key', 250, '', 'weixins');
INSERT INTO `common_info` VALUES (251, '2020-07-27 15:01:00', NULL, '师傅端微信商户号mchId', 251, '', 'weixin');
INSERT INTO `common_info` VALUES (252, '2023-07-07 18:14:01', NULL, '联系客服（电话）', 252, '', 'xitong');
INSERT INTO `common_info` VALUES (258, '2023-08-15 17:10:49', NULL, '支付宝方式 1证书 2秘钥', 258, '', 'zhifubao');
INSERT INTO `common_info` VALUES (262, '2020-07-09 17:59:45', NULL, '员工微信公众号APPID', 262, '', 'weixin');
INSERT INTO `common_info` VALUES (263, '2020-02-25 20:43:38', NULL, '员工微信公众号秘钥', 263, '', 'weixins');
INSERT INTO `common_info` VALUES (264, '2022-09-05 19:10:16', NULL, '微信证书路径', 264, '', 'weixin');
INSERT INTO `common_info` VALUES (265, '2020-02-25 20:43:38', NULL, '支付宝证书路径', 265, '', 'zhifubao');
INSERT INTO `common_info` VALUES (266, '2020-02-25 20:43:38', NULL, '支付宝证书appid', 266, '', 'zhifubao');
INSERT INTO `common_info` VALUES (267, '2020-02-25 20:43:38', NULL, '支付宝证书密钥', 267, '', 'zhifubao');
INSERT INTO `common_info` VALUES (271, '2022-01-01 19:00:49', NULL, '用户端腾讯地图名字', 271, '', 'weixin');
INSERT INTO `common_info` VALUES (274, '2023-07-07 18:17:29', NULL, '联系客服', 274, '', 'xitong');
INSERT INTO `common_info` VALUES (276, '2022-01-06 09:12:16', NULL, '用户端小程序分享文案', 276, '', 'xitong');
INSERT INTO `common_info` VALUES (277, '2022-09-16 20:21:01', NULL, '用户端小程序分享图', 277, '', 'image');
INSERT INTO `common_info` VALUES (280, '2022-03-16 10:16:57', NULL, '微信提现方式 1自动 2手动', 280, '', 'weixin');
INSERT INTO `common_info` VALUES (281, '2022-09-05 19:06:28', NULL, '微信证书地址url', 281, '', 'weixin');
INSERT INTO `common_info` VALUES (282, '2022-09-19 18:30:18', NULL, '师傅端腾讯地图key', 282, '', 'weixin');
INSERT INTO `common_info` VALUES (283, '2021-12-22 10:11:36', NULL, '师傅端腾讯地图名字', 283, '', 'weixin');
INSERT INTO `common_info` VALUES (289, '2020-09-23 15:53:12', NULL, 'APP师傅端消息推送PushAppKey', 289, '', 'appxiaoxi');
INSERT INTO `common_info` VALUES (290, '2020-09-23 15:53:24', NULL, 'APP师傅端消息推送PushAppId', 290, '', 'appxiaoxi');
INSERT INTO `common_info` VALUES (291, '2020-09-23 15:53:30', NULL, 'APP师傅端消息推送PushMasterSecret', 291, '', 'appxiaoxi');
INSERT INTO `common_info` VALUES (294, '2020-02-25 20:43:38', NULL, '师傅微信AppappId', 294, '', 'weixin');
INSERT INTO `common_info` VALUES (312, '2020-11-04 10:30:40', NULL, '通用APP下载地址	', 312, '', 'xitong');
INSERT INTO `common_info` VALUES (332, '2023-03-18 11:05:50', NULL, '发布文本以及图片的审核方式(1人工审核 2百度敏感词自动审核)', 332, '', 'xitong');
INSERT INTO `common_info` VALUES (341, '2022-11-07 14:02:02', NULL, '客服APPid', 341, '', 'xitong');
INSERT INTO `common_info` VALUES (342, '2022-11-07 14:02:10', NULL, '客服链接', 342, '', 'xitong');
INSERT INTO `common_info` VALUES (343, '2023-07-07 18:19:01', NULL, '客服联系方式 1 手机号 2企业微信 3客服二维码', 343, '', 'xitong');
INSERT INTO `common_info` VALUES (346, '2020-11-04 10:30:40', NULL, '订单多少分钟未支付自动取消', 346, '', 'xitong');
INSERT INTO `common_info` VALUES (351, '2023-08-18 13:58:53', NULL, '支付宝转账场景', 351, '', 'zhifubao');
INSERT INTO `common_info` VALUES (355, '2023-07-14 15:13:22', NULL, '是否开启微信提现开关', 355, '', 'kaiguan');
INSERT INTO `common_info` VALUES (356, '2023-07-14 15:13:22', NULL, '是否开启支付宝提现开关', 356, '', 'kaiguan');
INSERT INTO `common_info` VALUES (357, '2022-09-16 20:21:01', NULL, '师傅端小程序分享图', 357, '', 'image');
INSERT INTO `common_info` VALUES (358, '2022-01-06 09:12:16', NULL, '师傅端小程序分享文案', 358, '', 'xitong');
INSERT INTO `common_info` VALUES (360, '2023-07-19 16:36:22', NULL, '用户注销协议', 360, '', 'xieyi');
INSERT INTO `common_info` VALUES (414, '2023-07-26 16:27:15', NULL, '地图方式 1腾讯 2天地图', 414, '', 'xitong');
INSERT INTO `common_info` VALUES (415, '2023-07-26 16:27:15', NULL, '天地图key', 415, '', 'xitong');
INSERT INTO `common_info` VALUES (416, '2020-11-04 10:30:40', NULL, '师傅apk下载地址', 348, '', 'xitong');
INSERT INTO `common_info` VALUES (417, '2026-08-03 16:21:57', NULL, '订单退款扣费比例(0.1表示10%,不扣填0)', 417, '', 'xitong');
INSERT INTO `common_info` VALUES (418, '2023-08-18 13:58:53', NULL, '会员折扣（0.9表示九折）', 418, '', 'xitong');
INSERT INTO `common_info` VALUES (419, '2023-08-18 13:58:53', NULL, '官方邀请码', 419, '', 'xitong');
INSERT INTO `common_info` VALUES (507, '2023-03-18 11:05:50', NULL, '百度云apiKey', 507, '', 'xitong');
INSERT INTO `common_info` VALUES (508, '2023-03-18 11:05:50', NULL, '百度云secretKey', 508, '', 'xitong');
INSERT INTO `common_info` VALUES (509, '2023-03-18 11:05:50', NULL, '酒店服务协议', 509, '', 'xieyi');
INSERT INTO `common_info` VALUES (510, '2023-03-18 11:05:50', NULL, '关于我们', 510, '', 'xieyi');
INSERT INTO `common_info` VALUES (511, '2023-03-18 11:05:50', NULL, '开票说明', 511, '', 'xieyi');
INSERT INTO `common_info` VALUES (512, '2023-03-18 11:05:50', NULL, '客服类型：1电话 2微信', 512, '', 'xitong');
INSERT INTO `common_info` VALUES (513, '2023-03-18 11:05:50', NULL, '企业微信客服链接', 513, '', 'weixin');
INSERT INTO `common_info` VALUES (514, '2023-03-18 11:05:50', NULL, '企业微信客服appId', 514, '', 'weixin');
INSERT INTO `common_info` VALUES (515, '2023-03-18 11:05:50', NULL, '推广文案', 515, '', 'xitong');
INSERT INTO `common_info` VALUES (516, '2023-03-18 11:05:50', NULL, '邀请好友海报', 516, '', 'xitong');

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `opinion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '意见',
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '意见反馈' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of feedback
-- ----------------------------

-- ----------------------------
-- Table structure for help_article
-- ----------------------------
DROP TABLE IF EXISTS `help_article`;
CREATE TABLE `help_article`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` bigint(0) NULL DEFAULT NULL COMMENT '分类id',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帮助中心文章' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_article
-- ----------------------------

-- ----------------------------
-- Table structure for help_category
-- ----------------------------
DROP TABLE IF EXISTS `help_category`;
CREATE TABLE `help_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `client_type` int(0) NULL DEFAULT NULL COMMENT '客户端类型 1: 用户端',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帮助中心分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_category
-- ----------------------------
INSERT INTO `help_category` VALUES (1, '登录问题1', 1, 1, '2025-09-30 14:05:12', '2025-10-14 16:43:53');
INSERT INTO `help_category` VALUES (2, '会员问题1', 1, 2, '2025-09-30 14:05:27', '2025-10-14 16:44:03');

-- ----------------------------
-- Table structure for invoice
-- ----------------------------
DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单ID',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型 1个人 2公司',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态 1待开票 2已开票',
  `credit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '纳税人识别号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `open_bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '开户行',
  `bank_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '银行账号',
  `receive_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接收邮箱',
  `receive_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接收手机号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '发票' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invoice
-- ----------------------------
INSERT INTO `invoice` VALUES (18, 4, 24, 'dfsdfgdsfgdf', 'dfdsfdsg', 297.56, 1, 1, 'sdfsdfdsfg', 'dsfsgdfgdf', '18892020825', '111111111111', '111111111', '163.@com', '18892020825', '2026-05-29 14:03:25', NULL);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_img` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '特权名称',
  `sort` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
  `member_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '特权描述',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '会员特权表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (3, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/2ef451b3e0f7f98464b38430c13051b0.png', '所有课程', '1', NULL);
INSERT INTO `member` VALUES (4, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/37c225b26b3f011319de1cb4f1c18baf.png', '海量音乐', '2', NULL);
INSERT INTO `member` VALUES (6, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/d03a8a0f55240c438209777b896ebd94.png', '亲友体验卡', '3', NULL);
INSERT INTO `member` VALUES (9, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/071892b060308ae7f0c7275d26d92f80.png', '自由练习', '5', NULL);
INSERT INTO `member` VALUES (10, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/b9e72742a7bba9a20cf2d13906af441c.png', '专属客服', '4', NULL);
INSERT INTO `member` VALUES (11, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/423a302253532b6a993788efab1ce8f7.png', 'VIP标识', '6', NULL);
INSERT INTO `member` VALUES (12, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/dfb149846d4cc92a9f00d261d4c3cca1.png', '专属奖励', '7', NULL);

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否已读 0未',
  `send_state` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送状态',
  `send_time` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送时间',
  `state` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类  4订单消息  5 用户消息',
  `title` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `type` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息来源',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `audit_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `status` int(0) NULL DEFAULT NULL COMMENT '审核状态 0未 1通 2拒',
  `by_user_id` int(0) NULL DEFAULT NULL COMMENT '发送给的用户id',
  `platform_id` int(0) NULL DEFAULT NULL COMMENT '相关id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = big5 COLLATE = big5_chinese_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_info
-- ----------------------------
INSERT INTO `message_info` VALUES (1, '小程序账号绑定成功！', '2026-07-21 14:04:24', NULL, '0', NULL, NULL, '5', '系统通知', NULL, NULL, NULL, '24', '188****0825', NULL, NULL, NULL, NULL);
INSERT INTO `message_info` VALUES (2, '商家入驻审核结果: 通过', '2026-07-28 15:47:12', NULL, '0', NULL, NULL, '5', '商家入驻审核', NULL, NULL, NULL, '31', '张一', NULL, NULL, NULL, NULL);
INSERT INTO `message_info` VALUES (3, '撒的', '2026-07-31 10:22:27', NULL, '0', NULL, NULL, '5', '提现失败提醒', NULL, NULL, NULL, '31', '张十一', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '验证码id',
  `code` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '验证码',
  `phone` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`code`, `phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = big5 COLLATE = big5_chinese_ci COMMENT = '验证码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_type` int(0) NULL DEFAULT NULL COMMENT '订单类型 1酒店 2民宿',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '下单用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下单用户昵称',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `user_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下单用户手机号',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `shop_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '酒店名称',
  `cover_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '酒店封面图',
  `room_id` int(0) NULL DEFAULT NULL COMMENT '房间id',
  `room_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房间名称',
  `warm_reminder` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '酒店的温馨提示',
  `room_count` int(0) NULL DEFAULT NULL COMMENT '房间数',
  `start_date` date NULL DEFAULT NULL COMMENT '入住时间',
  `end_date` date NULL DEFAULT NULL COMMENT '离店时间',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `order_status` int(0) NULL DEFAULT NULL COMMENT '订单状态 0待支付 1待核销 2进行中 3已完成 4已取消',
  `arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '到店时间',
  `coupon_id` int(0) NULL DEFAULT NULL COMMENT '优惠券id',
  `coupon_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `order_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `pay_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `refund_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `shop_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '酒店收入',
  `platform_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '平台收入',
  `user_scale_user_id` int(0) NULL DEFAULT NULL COMMENT '上级id',
  `user_scale_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '上级抽佣金额',
  `user_two_user_id` int(0) NULL DEFAULT NULL COMMENT '上上级id',
  `user_two_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '上上级抽佣金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `pay_way` int(0) NULL DEFAULT NULL COMMENT '支付方式0余额 1微信 2支付宝',
  `is_pay` int(0) NULL DEFAULT NULL COMMENT '是否已支付0否 1是',
  `is_evaluate` int(0) NULL DEFAULT NULL COMMENT '是否已评价0否 1是',
  `is_vip_order` int(0) NULL DEFAULT NULL COMMENT '是否是会员订单 0否 1是',
  `verification_user_id` int(0) NULL DEFAULT NULL COMMENT '核销人id',
  `verification_time` datetime(0) NULL DEFAULT NULL COMMENT '核销时间',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '订单备注',
  `is_invoice` int(0) NULL DEFAULT 1 COMMENT '是否开票 1=未开票 2=开票中 3=已开票',
  `check_invoice` int(0) NULL DEFAULT 2 COMMENT '是否勾选发票信息：1是 2否',
  `user_info_id` int(0) NULL DEFAULT NULL COMMENT '常用人信息id',
  `room_type_id` int(0) NULL DEFAULT NULL COMMENT '房间类型id',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for pay_classify
-- ----------------------------
DROP TABLE IF EXISTS `pay_classify`;
CREATE TABLE `pay_classify`  (
  `pay_classify_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '充值分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `give_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '赠送数量',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '水晶数量',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  `is_enable` int(0) NULL DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_classify
-- ----------------------------

-- ----------------------------
-- Table structure for pay_details
-- ----------------------------
DROP TABLE IF EXISTS `pay_details`;
CREATE TABLE `pay_details`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '充值记录id',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付订单号',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `classify` int(0) NULL DEFAULT NULL COMMENT '支付类型 1微信app支付  2微信公众号支付  3微信小程序支付 4支付宝app  5支付宝H5',
  `type` int(0) NULL DEFAULT NULL COMMENT '分类 1.订单支付 2余额充值 3开通会员',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `state` int(0) NULL DEFAULT 0 COMMENT '0待支付 1支付成功 2失败',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付宝交易订单号',
  `relation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关联id',
  `refund_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '充值记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_details
-- ----------------------------

-- ----------------------------
-- Table structure for recharge_config
-- ----------------------------
DROP TABLE IF EXISTS `recharge_config`;
CREATE TABLE `recharge_config`  (
  `recharge_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `coupon_id` int(0) NULL DEFAULT NULL COMMENT '优惠券id',
  `coupon_num` int(0) NULL DEFAULT NULL COMMENT '优惠券数量',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '是否开启(0否 1是)',
  PRIMARY KEY (`recharge_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充值配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recharge_config
-- ----------------------------
INSERT INTO `recharge_config` VALUES (2, 0.01, NULL, 0, 1, 1);
INSERT INTO `recharge_config` VALUES (3, 0.01, 15, 1, 2, 1);
INSERT INTO `recharge_config` VALUES (4, 100.00, 1, 10, 3, 1);
INSERT INTO `recharge_config` VALUES (5, 200.00, 1, 20, 4, 1);
INSERT INTO `recharge_config` VALUES (6, 500.00, 1, 50, 5, 1);

-- ----------------------------
-- Table structure for room_device_classify
-- ----------------------------
DROP TABLE IF EXISTS `room_device_classify`;
CREATE TABLE `room_device_classify`  (
  `classify_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '选项id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_device_classify
-- ----------------------------
INSERT INTO `room_device_classify` VALUES (1, '独立卫生间', 'https://zp.xianmaxiong.com/file/uploadPath/2023/02/28/cc91e286b6772c8ebb15540f5c97cbe1.jpeg', 1, '2023-08-11 14:27:05');
INSERT INTO `room_device_classify` VALUES (2, '免费空调', 'https://zp.xianmaxiong.com/file/uploadPath/2023/02/28/cc91e286b6772c8ebb15540f5c97cbe1.jpeg', 2, '2023-08-11 14:27:26');
INSERT INTO `room_device_classify` VALUES (3, '独立浴室', 'https://zp.xianmaxiong.com/file/uploadPath/2023/02/28/cc91e286b6772c8ebb15540f5c97cbe1.jpeg', 3, '2023-08-11 14:27:37');
INSERT INTO `room_device_classify` VALUES (6, '免费客房WIFI', 'https://zp.xianmaxiong.com/file/uploadPath/2023/02/28/cc91e286b6772c8ebb15540f5c97cbe1.jpeg', 5, '2023-08-11 14:31:38');

-- ----------------------------
-- Table structure for room_order_record
-- ----------------------------
DROP TABLE IF EXISTS `room_order_record`;
CREATE TABLE `room_order_record`  (
  `record_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `order_id` int(0) NULL DEFAULT NULL COMMENT '订单id',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `room_id` int(0) NULL DEFAULT NULL COMMENT '房间类型id',
  `room_count` int(0) NULL DEFAULT NULL COMMENT '房间占用数量',
  `start_time` date NULL DEFAULT NULL COMMENT '入驻时间',
  `end_time` date NULL DEFAULT NULL COMMENT '离店时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单房间记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_order_record
-- ----------------------------
INSERT INTO `room_order_record` VALUES (16, 19, 439, 9, 1, '2026-07-29', '2026-07-30', '2026-07-29 19:58:32');
INSERT INTO `room_order_record` VALUES (17, 20, 436, 6, 1, '2026-07-29', '2026-07-30', '2026-07-29 20:00:48');

-- ----------------------------
-- Table structure for sc_follow
-- ----------------------------
DROP TABLE IF EXISTS `sc_follow`;
CREATE TABLE `sc_follow`  (
  `follow_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '关注id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `by_id` int(0) NULL DEFAULT NULL COMMENT '被关注的id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `classify` int(0) NULL DEFAULT NULL COMMENT '类型 1商品收藏 2商品想要 3关注用户 4收藏动态',
  PRIMARY KEY (`follow_id`) USING BTREE,
  INDEX `delete_shop`(`by_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_follow
-- ----------------------------
INSERT INTO `sc_follow` VALUES (2, 24, 2, '2026-05-13 19:24:32', 1);

-- ----------------------------
-- Table structure for shop_money_details
-- ----------------------------
DROP TABLE IF EXISTS `shop_money_details`;
CREATE TABLE `shop_money_details`  (
  `details_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '明细id',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '员工id',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `type` int(0) NULL DEFAULT NULL COMMENT '1增加 2减少',
  `classify` int(0) NULL DEFAULT NULL COMMENT '类型 1收入 2提现 3退款',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`details_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '酒店钱包明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_money_details
-- ----------------------------

-- ----------------------------
-- Table structure for shops
-- ----------------------------
DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops`  (
  `shop_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '酒店id',
  `shop_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '酒店名称',
  `title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '封面图',
  `carousel_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '轮播图',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `county` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `address` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `lng` double(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `is_enable` int(0) NULL DEFAULT 1 COMMENT '状态 0下架 1上架',
  `score` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '评分',
  `is_recommend` int(0) NULL DEFAULT 1 COMMENT '是否是推荐0否 1是',
  `tags` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `rate` decimal(2, 2) NULL DEFAULT NULL COMMENT '分成比例',
  `all_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '总收益',
  `cash_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '可提现金额',
  `cash_down_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '已提现金额',
  `shop_type` int(0) NULL DEFAULT NULL COMMENT '酒店类型 1酒店 2民宿',
  `audit_status` int(0) NULL DEFAULT 3 COMMENT '酒店审核状态：1已通过 2已拒绝 3待审核',
  `audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `shop_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '酒店联系电话',
  PRIMARY KEY (`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 442 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '酒店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shops
-- ----------------------------

-- ----------------------------
-- Table structure for shops_room
-- ----------------------------
DROP TABLE IF EXISTS `shops_room`;
CREATE TABLE `shops_room`  (
  `room_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '房间id',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `shop_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '酒店名称',
  `room_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房间名称',
  `room_type_id` int(0) NULL DEFAULT NULL COMMENT '房间类型id',
  `room_count` int(0) NULL DEFAULT NULL COMMENT '房间数量',
  `room_title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '房间封面图',
  `room_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '房间轮播图',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价格',
  `classify_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置ids',
  `check_in_time` time(0) NULL DEFAULT NULL COMMENT '入住时间',
  `check_out_time` time(0) NULL DEFAULT NULL COMMENT '离店时间',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `food_policy` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '餐食规则',
  `pet_rule` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '宠物规则',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '是否启用0否 1是',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `room_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房间类型名称',
  `member_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员价格',
  `hotel_tips` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '酒店提示',
  `floor_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房屋面积',
  `floor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '楼层',
  `room_status` int(0) NULL DEFAULT NULL COMMENT '房间状态：1空闲  2使用中 ',
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shops_room
-- ----------------------------
INSERT INTO `shops_room` VALUES (1, 2, '西安赛默奇酒店', '丽质大床房', 1, 10, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/c1869d73631447c60fe5c44f8b669380.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/a1b138fe21e7fd58f264a9f056a0c4ee.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/de10dfb63ce6ab752e8d02e096ad7f87.jpg', 148.00, '1,2,3,6', '00:00:00', '12:00:00', NULL, '这是餐食规则', '这是宠物规则', 1, '2026-08-03 10:05:59', '2023-08-11 14:55:53', '大床房', NULL, NULL, NULL, NULL, 1);
INSERT INTO `shops_room` VALUES (4, 2, '西安赛默奇酒店', '丽质大床房', 1, 10, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/c1869d73631447c60fe5c44f8b669380.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/a1b138fe21e7fd58f264a9f056a0c4ee.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/de10dfb63ce6ab752e8d02e096ad7f87.jpg', 56.00, '1,2,3,6', '00:00:00', '12:00:00', NULL, '这是餐食规则', '这是宠物规则', 1, '2026-08-03 10:05:58', '2023-08-11 16:02:10', '大床房', NULL, NULL, NULL, NULL, 1);
INSERT INTO `shops_room` VALUES (5, 2, '西安赛默奇酒店', '丽质大床房', 2, 10, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/c1869d73631447c60fe5c44f8b669380.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/a1b138fe21e7fd58f264a9f056a0c4ee.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/de10dfb63ce6ab752e8d02e096ad7f87.jpg', 148.78, '1,2,3,6', '00:00:00', '12:00:00', NULL, '这是餐食规则', '这是宠物规则', 1, '2026-08-03 10:05:59', '2023-08-11 16:02:17', '钟点房', NULL, NULL, NULL, NULL, 1);
INSERT INTO `shops_room` VALUES (6, 436, '简单酒店', '极简大床房', 2, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/c1869d73631447c60fe5c44f8b669380.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/a1b138fe21e7fd58f264a9f056a0c4ee.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/de10dfb63ce6ab752e8d02e096ad7f87.jpg', 120.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '这里是蚕食成册', '这里是洪武政策', 1, '2026-08-03 10:05:59', '2026-06-04 17:43:52', '钟点房', 110.00, '这里是酒店提示', '25', '4', 2);
INSERT INTO `shops_room` VALUES (7, 436, '简单酒店', '电竞房', 1, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/f0b17deba0a7ab9945db4a0dd0b321f7.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/f0b17deba0a7ab9945db4a0dd0b321f7.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/5e34dd5ff82043850551a727de485d22.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/08afc59a8e2e672b8f154cbc09adf246.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/070098bde735218942fefd0243bfe1f9.jpg', 156.00, '1,2,3,6', '12:00:00', '20:00:00', NULL, '这里是餐食政策', '这里是宠物政策', 1, '2026-08-03 10:06:07', '2026-06-08 10:14:02', '大床房', 138.00, '这里是酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (8, 439, '西安维也纳国际大酒店', '大床房不带浴盆', 1, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/c1869d73631447c60fe5c44f8b669380.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/a1b138fe21e7fd58f264a9f056a0c4ee.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/de10dfb63ce6ab752e8d02e096ad7f87.jpg', 100.00, '1,2,3,6', '12:00:00', '14:00:00', NULL, '餐食政策餐食政策餐食政策', '宠物政策宠物政策宠物政策', 1, '2026-08-03 10:05:58', '2026-06-23 14:33:55', '大床房', 80.00, '酒店提示酒店提示酒店提示', '20', '1', 1);
INSERT INTO `shops_room` VALUES (9, 439, '西安维也纳国际大酒店', '标间高层带棋牌室', 2, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/3566712ba04d00aa730110c28287bb52.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/3566712ba04d00aa730110c28287bb52.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/da867aad2bfb82f388721341145a6d06.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2a1cc2ff8751e76fb31af85589bd5258.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d9e4a7d0083c1a72b6f86559e07351cd.jpg', 200.00, '1,2,3,6', '12:00:00', '14:00:00', NULL, '餐食政策测男阿斯达丢三落四肯定', '宠物政策覅发生的辣三丁卡洛斯', 1, '2026-08-03 10:06:42', '2026-06-24 14:25:57', '钟点房', 160.00, '酒店提示爱仕达十六大石达开十九大', '50', '20', 2);
INSERT INTO `shops_room` VALUES (10, 441, '八天连锁酒店', '大床房临街房', 1, 2, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/2e1baca9f1bcd8de1d8842f5d4a9d964.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/c1869d73631447c60fe5c44f8b669380.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/a1b138fe21e7fd58f264a9f056a0c4ee.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/de10dfb63ce6ab752e8d02e096ad7f87.jpg', 120.00, '1,2,6', '11:30:00', '14:30:00', NULL, '没有参数', '么一宠物', 1, '2026-08-04 16:38:23', '2026-07-28 18:38:49', '大床房', 100.00, '宠物提示', '25', '16', 1);
INSERT INTO `shops_room` VALUES (14, 436, '简单酒店', '极简双床标间-1', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-05 00:00:01', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (15, 436, '简单酒店', '极简双床标间-2', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:20', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (16, 436, '简单酒店', '极简双床标间-3', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:20', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (17, 436, '简单酒店', '极简双床标间-4', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:20', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (18, 436, '简单酒店', '极简双床标间-5', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:20', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (19, 436, '简单酒店', '极简双床标间-6', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:21', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (20, 436, '简单酒店', '极简双床标间-7', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:21', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (21, 436, '简单酒店', '极简双床标间-8', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-05 00:00:02', '2026-08-03 09:54:45', '标间', 145.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (22, 436, '简单酒店', '极简双床标间-9', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:22', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (23, 436, '简单酒店', '极简双床标间-10', 4, 1, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/d954043a0946eac4628ff381733aab3e.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/4bf6b05ac1d8a8805615aa465a3889f1.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/bb168f12db0536b500cd00244c60c693.jpg,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/ffe5ce143c89cc3f5d2bf304575e4e0b.jpg', 165.00, '1,2,3,6', '12:00:00', '12:00:00', NULL, '标间的餐食政策', '标间的宠物政策', 1, '2026-08-03 10:06:22', '2026-08-03 09:54:45', '标间', 135.00, '标间的酒店提示', '35', '3', 1);
INSERT INTO `shops_room` VALUES (24, 441, '八天连锁酒店', '标准间低楼层', 4, 2, 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/eb7812a48f91498188c469a0af834893.png', 'https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/74bee30b4b32e526c02032053250bfd1.png,https://jdms.xianmaxiong.com/file/uploadPath/2026/08/03/8f838d5cee69c935a039228b1d88e65f.png', 200.00, '1,2,3,6', '12:00:00', '14:00:00', NULL, '出纳室政策', '宠物政策宠物政策', 1, '2026-08-04 16:42:52', '2026-08-03 15:46:49', '标间', 150.00, '酒店提示酒店提示', '35', NULL, 1);

-- ----------------------------
-- Table structure for shops_room_type
-- ----------------------------
DROP TABLE IF EXISTS `shops_room_type`;
CREATE TABLE `shops_room_type`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '房间类型表id',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shops_room_type
-- ----------------------------
INSERT INTO `shops_room_type` VALUES (1, '大床房', 1, '2026-05-13 16:37:42');
INSERT INTO `shops_room_type` VALUES (2, '钟点房', 2, '2026-05-13 16:37:42');
INSERT INTO `shops_room_type` VALUES (4, '标间', NULL, '2026-08-03 09:53:12');

-- ----------------------------
-- Table structure for shops_user_role
-- ----------------------------
DROP TABLE IF EXISTS `shops_user_role`;
CREATE TABLE `shops_user_role`  (
  `role_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户酒店权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shops_user_role
-- ----------------------------
INSERT INTO `shops_user_role` VALUES (12, 22, 2, '2023-08-11 10:58:26');
INSERT INTO `shops_user_role` VALUES (15, 23, 3, '2026-03-19 18:06:21');
INSERT INTO `shops_user_role` VALUES (16, 25, 436, '2026-06-08 17:43:54');
INSERT INTO `shops_user_role` VALUES (17, 27, 439, '2026-06-23 15:21:55');
INSERT INTO `shops_user_role` VALUES (19, 32, 441, '2026-08-03 14:06:36');

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `sys_evaluate`;
CREATE TABLE `sys_evaluate`  (
  `evaluate_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id(评分人)',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `order_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `evaluate_message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评价内容',
  `satisfaction_flag` int(0) NULL DEFAULT NULL COMMENT '评级 1:非常差 2:差 3:一般 4:满意 5:非常满意',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `evaluate_img` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '评价图片',
  `is_delete` int(0) NULL DEFAULT 0 COMMENT '是否已删除0否 -1是',
  PRIMARY KEY (`evaluate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3209 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_evaluate
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(0) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 'nyy', '修改菜单', 'com.sqx.modules.sys.controller.SysMenuController.update()', '[{\"menuId\":341,\"parentId\":0,\"name\":\"酒店列表\",\"url\":\"universalMission\",\"type\":1,\"icon\":\"shouye\",\"orderNum\":6}]', 9, '192.168.0.109', '2023-08-21 14:17:55');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 392 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2, 340, '管理员列表', 'user', NULL, 1, 'UserFilled', 1);
INSERT INTO `sys_menu` VALUES (3, 340, '角色管理', 'role', NULL, 1, 'User', 2);
INSERT INTO `sys_menu` VALUES (4, 340, '菜单管理', 'menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 0, '用户中心', 'userList', NULL, 1, 'Avatar', 2);
INSERT INTO `sys_menu` VALUES (34, 0, '财务中心', 'financeList', '', 1, 'Money', 2);
INSERT INTO `sys_menu` VALUES (39, 0, '系统配置', 'allocationList', '', 1, 'Aim', 19);
INSERT INTO `sys_menu` VALUES (40, 0, '数据中心', 'home', '', 1, 'Calendar', 1);
INSERT INTO `sys_menu` VALUES (46, 32, '查看', '', 'userList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (47, 32, '删除', '', 'userList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (48, 34, '查看', '', 'financeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (49, 34, '转账', '', 'financeList:transfer', 2, '', 0);
INSERT INTO `sys_menu` VALUES (50, 34, '退款', '', 'financeList:refund', 2, '', 0);
INSERT INTO `sys_menu` VALUES (82, 39, '查看', '', 'allocationList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (83, 39, '修改', '', 'allocationList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (187, 0, '配置管理', 'couponCard', '4', 1, 'Coin', 3);
INSERT INTO `sys_menu` VALUES (188, 187, '查看', '', 'couponCard:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (189, 187, '添加', '', 'couponCard:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (190, 187, '修改', '', 'couponCard:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (191, 187, '删除', '', 'couponCard:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (202, 32, '修改金额', '', 'userList:updateMoney', 2, '', 0);
INSERT INTO `sys_menu` VALUES (203, 32, '修改状态', '', 'userList:updateStatus', 2, '', 0);
INSERT INTO `sys_menu` VALUES (256, 0, '帮助中心', 'materialsList', '', 1, 'ChromeFilled', 9);
INSERT INTO `sys_menu` VALUES (257, 256, '查看', '', 'materialsList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (258, 256, '添加', '', 'materialLink:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (259, 256, '修改', '', 'materialLink:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (260, 256, '删除', '', 'materialLink:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (261, 0, '升级配置', 'app', '', 1, 'config', 13);
INSERT INTO `sys_menu` VALUES (262, 261, '查看', '', 'app:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (263, 261, '添加', '', 'app:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (264, 261, '修改', '', 'app:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (265, 261, '删除', '', 'app:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (276, 32, '设置会员', '', 'userList:updateVip', 2, '', 0);
INSERT INTO `sys_menu` VALUES (288, 0, '聊天记录', 'chatRecord', NULL, 1, 'order', 8);
INSERT INTO `sys_menu` VALUES (289, 288, '查看', NULL, 'chatRecord:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (290, 288, '删除', NULL, 'chatRecord:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (321, 0, '卡密管理', 'coupon', NULL, 1, 'fenleilist', 5);
INSERT INTO `sys_menu` VALUES (322, 321, '查看', 'coupon:list', 'coupon:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (323, 321, '添加', 'coupon:list', 'coupon:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (324, 321, '修改', 'coupon:list', 'coupon:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (325, 321, '删除', 'coupon:list', 'coupon:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (326, 321, '生成', 'coupon:list', 'coupon:shengcheng', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (332, 0, '分类管理', 'pattern', NULL, 1, 'order', 4);
INSERT INTO `sys_menu` VALUES (333, 332, '查看', NULL, 'pattern:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (334, 332, '添加', NULL, 'pattern:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (335, 332, '修改', NULL, 'pattern:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (336, 332, '删除', NULL, 'pattern:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (337, 0, '订单中心', 'orderCenter', NULL, 1, 'Document', 2);
INSERT INTO `sys_menu` VALUES (338, 337, '查看', NULL, 'orderCenter:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (339, 32, '修改信息', NULL, 'userList:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (340, 0, '系统管理', NULL, NULL, 0, 'Tools', 20);
INSERT INTO `sys_menu` VALUES (341, 0, '酒店列表', 'locality', NULL, 1, 'School', 6);
INSERT INTO `sys_menu` VALUES (342, 341, '查看', NULL, 'locality:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (343, 341, '添加', NULL, 'locality:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (344, 341, '修改', NULL, 'locality:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (345, 341, '删除', NULL, 'locality:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (346, 0, '会员配置', 'coupon', NULL, 1, 'Help', 6);
INSERT INTO `sys_menu` VALUES (347, 346, '查看', NULL, 'coupon:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (348, 346, '添加', NULL, 'coupon:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (349, 346, '修改', NULL, 'coupon:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (350, 346, '删除', NULL, 'coupon:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (351, 0, '会员特权', 'vipPrivilege', NULL, 1, 'Finished', 6);
INSERT INTO `sys_menu` VALUES (352, 351, '添加', NULL, 'vipPrivilege:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (353, 351, '添加', NULL, 'vipPrivilege:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (354, 351, '修改', NULL, 'vipPrivilege:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (355, 351, '删除', NULL, 'vipPrivilege:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (356, 371, '优惠券', 'couponYhq', NULL, 1, 'CreditCard', 1);
INSERT INTO `sys_menu` VALUES (357, 356, '查看', NULL, 'couponYhq:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (358, 356, '添加', NULL, 'couponYhq:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (359, 356, '修改', NULL, 'couponYhq:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (360, 356, '删除', NULL, 'couponYhq:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (361, 371, '优惠券领取记录', 'couponuser', NULL, 1, 'Document', 2);
INSERT INTO `sys_menu` VALUES (362, 361, '查看', NULL, 'couponuser:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (363, 0, '推广中心', 'agent', NULL, 1, 'CollectionTag', 6);
INSERT INTO `sys_menu` VALUES (364, 363, '查看', NULL, 'agent:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (365, 363, '添加', NULL, 'agent:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (366, 363, '修改', NULL, 'agent:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (367, 363, '删除', NULL, 'agent:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (368, 361, '添加', NULL, 'eulogy:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (369, 361, '修改', NULL, 'eulogy:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (370, 361, '删除', NULL, 'eulogy:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (371, 0, '优惠券管理', NULL, NULL, 0, 'Goods', 6);
INSERT INTO `sys_menu` VALUES (372, 0, '时间配置', 'creditScore', NULL, 1, 'job', 6);
INSERT INTO `sys_menu` VALUES (373, 372, '查看', NULL, 'creditScore:list', 2, 'shezhi', 6);
INSERT INTO `sys_menu` VALUES (374, 372, '添加', NULL, 'creditScore:add', 2, 'shezhi', 6);
INSERT INTO `sys_menu` VALUES (375, 372, '修改', NULL, 'creditScore:update', 2, 'shezhi', 6);
INSERT INTO `sys_menu` VALUES (376, 372, '删除', NULL, 'creditScore:delete', 2, 'shezhi', 6);
INSERT INTO `sys_menu` VALUES (377, 0, '商家入驻', 'country', NULL, 1, 'MessageBox', 7);
INSERT INTO `sys_menu` VALUES (378, 377, '查看', NULL, 'country:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (379, 40, '查看', NULL, 'home:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (380, 0, '评论中心', 'commentList', NULL, 1, 'ChatLineSquare', 10);
INSERT INTO `sys_menu` VALUES (381, 380, '查看', NULL, 'commentList:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (382, 0, '发票管理', 'travelProducts', NULL, 1, 'CreditCard', 7);
INSERT INTO `sys_menu` VALUES (383, 382, '查看', NULL, 'travelProducts:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (384, 0, '注销信息', 'messageZx', NULL, 1, 'ChatLineSquare', 7);
INSERT INTO `sys_menu` VALUES (385, 384, '查看', NULL, 'messageZx:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (386, 384, '注销', NULL, 'messageZx:zhuxiao', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (387, 0, '消息中心', 'message', NULL, 1, 'ChatLineRound', 7);
INSERT INTO `sys_menu` VALUES (388, 387, '查看', NULL, 'message:list', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (389, 0, '首页装修', 'bannerList', NULL, 1, 'Picture', 5);
INSERT INTO `sys_menu` VALUES (390, 389, '查看', NULL, 'bannerList:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (391, 389, '修改', NULL, 'bannerList:update', 2, NULL, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(0) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (3, '超级管理员', '超级管理员', 38, '2020-08-10 14:28:56');
INSERT INTO `sys_role` VALUES (4, '普通管理员', '普通管理员', 34, '2020-08-10 14:35:08');
INSERT INTO `sys_role` VALUES (5, '运营人员', '运营人员', 1, '2020-08-10 14:35:29');
INSERT INTO `sys_role` VALUES (12, '店铺管理员', '店铺管理员', 34, '2021-12-20 14:05:35');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11650 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (123, 7, 0);
INSERT INTO `sys_role_menu` VALUES (4018, 5, 40);
INSERT INTO `sys_role_menu` VALUES (4019, 5, 46);
INSERT INTO `sys_role_menu` VALUES (4020, 5, 48);
INSERT INTO `sys_role_menu` VALUES (4030, 5, 82);
INSERT INTO `sys_role_menu` VALUES (4031, 5, 15);
INSERT INTO `sys_role_menu` VALUES (4032, 5, 19);
INSERT INTO `sys_role_menu` VALUES (4033, 5, 23);
INSERT INTO `sys_role_menu` VALUES (4034, 5, -666666);
INSERT INTO `sys_role_menu` VALUES (4035, 5, 32);
INSERT INTO `sys_role_menu` VALUES (4036, 5, 34);
INSERT INTO `sys_role_menu` VALUES (4038, 5, 39);
INSERT INTO `sys_role_menu` VALUES (4039, 5, 1);
INSERT INTO `sys_role_menu` VALUES (4040, 5, 2);
INSERT INTO `sys_role_menu` VALUES (4041, 5, 3);
INSERT INTO `sys_role_menu` VALUES (4042, 5, 4);
INSERT INTO `sys_role_menu` VALUES (4352, 10, 40);
INSERT INTO `sys_role_menu` VALUES (4353, 10, 32);
INSERT INTO `sys_role_menu` VALUES (4354, 10, 46);
INSERT INTO `sys_role_menu` VALUES (4355, 10, 47);
INSERT INTO `sys_role_menu` VALUES (4356, 10, 34);
INSERT INTO `sys_role_menu` VALUES (4357, 10, 48);
INSERT INTO `sys_role_menu` VALUES (4358, 10, 49);
INSERT INTO `sys_role_menu` VALUES (4359, 10, 50);
INSERT INTO `sys_role_menu` VALUES (4387, 10, 39);
INSERT INTO `sys_role_menu` VALUES (4388, 10, 82);
INSERT INTO `sys_role_menu` VALUES (4389, 10, 83);
INSERT INTO `sys_role_menu` VALUES (4390, 10, 1);
INSERT INTO `sys_role_menu` VALUES (4391, 10, 2);
INSERT INTO `sys_role_menu` VALUES (4392, 10, 15);
INSERT INTO `sys_role_menu` VALUES (4393, 10, 16);
INSERT INTO `sys_role_menu` VALUES (4394, 10, 17);
INSERT INTO `sys_role_menu` VALUES (4395, 10, 18);
INSERT INTO `sys_role_menu` VALUES (4396, 10, 3);
INSERT INTO `sys_role_menu` VALUES (4397, 10, 19);
INSERT INTO `sys_role_menu` VALUES (4398, 10, 20);
INSERT INTO `sys_role_menu` VALUES (4399, 10, 21);
INSERT INTO `sys_role_menu` VALUES (4400, 10, 22);
INSERT INTO `sys_role_menu` VALUES (4401, 10, 4);
INSERT INTO `sys_role_menu` VALUES (4402, 10, 23);
INSERT INTO `sys_role_menu` VALUES (4403, 10, 24);
INSERT INTO `sys_role_menu` VALUES (4404, 10, 25);
INSERT INTO `sys_role_menu` VALUES (4405, 10, 26);
INSERT INTO `sys_role_menu` VALUES (4406, 10, -666666);
INSERT INTO `sys_role_menu` VALUES (6878, 12, -666666);
INSERT INTO `sys_role_menu` VALUES (9197, 4, 40);
INSERT INTO `sys_role_menu` VALUES (9198, 4, 276);
INSERT INTO `sys_role_menu` VALUES (9199, 4, 46);
INSERT INTO `sys_role_menu` VALUES (9200, 4, 202);
INSERT INTO `sys_role_menu` VALUES (9201, 4, 203);
INSERT INTO `sys_role_menu` VALUES (9202, 4, 34);
INSERT INTO `sys_role_menu` VALUES (9203, 4, 48);
INSERT INTO `sys_role_menu` VALUES (9204, 4, 49);
INSERT INTO `sys_role_menu` VALUES (9205, 4, 50);
INSERT INTO `sys_role_menu` VALUES (9239, 4, 188);
INSERT INTO `sys_role_menu` VALUES (9240, 4, 189);
INSERT INTO `sys_role_menu` VALUES (9241, 4, 190);
INSERT INTO `sys_role_menu` VALUES (9280, 4, 288);
INSERT INTO `sys_role_menu` VALUES (9281, 4, 289);
INSERT INTO `sys_role_menu` VALUES (9282, 4, 290);
INSERT INTO `sys_role_menu` VALUES (9286, 4, 256);
INSERT INTO `sys_role_menu` VALUES (9287, 4, 257);
INSERT INTO `sys_role_menu` VALUES (9288, 4, 258);
INSERT INTO `sys_role_menu` VALUES (9289, 4, 259);
INSERT INTO `sys_role_menu` VALUES (9290, 4, 260);
INSERT INTO `sys_role_menu` VALUES (9291, 4, 82);
INSERT INTO `sys_role_menu` VALUES (9292, 4, 262);
INSERT INTO `sys_role_menu` VALUES (9293, 4, 15);
INSERT INTO `sys_role_menu` VALUES (9294, 4, 19);
INSERT INTO `sys_role_menu` VALUES (9295, 4, 23);
INSERT INTO `sys_role_menu` VALUES (9296, 4, -666666);
INSERT INTO `sys_role_menu` VALUES (9297, 4, 32);
INSERT INTO `sys_role_menu` VALUES (9308, 4, 187);
INSERT INTO `sys_role_menu` VALUES (9312, 4, 39);
INSERT INTO `sys_role_menu` VALUES (9313, 4, 1);
INSERT INTO `sys_role_menu` VALUES (9314, 4, 261);
INSERT INTO `sys_role_menu` VALUES (9315, 4, 2);
INSERT INTO `sys_role_menu` VALUES (9316, 4, 3);
INSERT INTO `sys_role_menu` VALUES (9317, 4, 4);
INSERT INTO `sys_role_menu` VALUES (11302, 1, 389);
INSERT INTO `sys_role_menu` VALUES (11303, 1, 390);
INSERT INTO `sys_role_menu` VALUES (11304, 1, 391);
INSERT INTO `sys_role_menu` VALUES (11565, 3, 40);
INSERT INTO `sys_role_menu` VALUES (11566, 3, 379);
INSERT INTO `sys_role_menu` VALUES (11567, 3, 32);
INSERT INTO `sys_role_menu` VALUES (11568, 3, 276);
INSERT INTO `sys_role_menu` VALUES (11569, 3, 46);
INSERT INTO `sys_role_menu` VALUES (11570, 3, 47);
INSERT INTO `sys_role_menu` VALUES (11571, 3, 339);
INSERT INTO `sys_role_menu` VALUES (11572, 3, 202);
INSERT INTO `sys_role_menu` VALUES (11573, 3, 203);
INSERT INTO `sys_role_menu` VALUES (11574, 3, 34);
INSERT INTO `sys_role_menu` VALUES (11575, 3, 48);
INSERT INTO `sys_role_menu` VALUES (11576, 3, 49);
INSERT INTO `sys_role_menu` VALUES (11577, 3, 50);
INSERT INTO `sys_role_menu` VALUES (11578, 3, 337);
INSERT INTO `sys_role_menu` VALUES (11579, 3, 338);
INSERT INTO `sys_role_menu` VALUES (11580, 3, 187);
INSERT INTO `sys_role_menu` VALUES (11581, 3, 188);
INSERT INTO `sys_role_menu` VALUES (11582, 3, 189);
INSERT INTO `sys_role_menu` VALUES (11583, 3, 190);
INSERT INTO `sys_role_menu` VALUES (11584, 3, 191);
INSERT INTO `sys_role_menu` VALUES (11585, 3, 389);
INSERT INTO `sys_role_menu` VALUES (11586, 3, 390);
INSERT INTO `sys_role_menu` VALUES (11587, 3, 391);
INSERT INTO `sys_role_menu` VALUES (11588, 3, 341);
INSERT INTO `sys_role_menu` VALUES (11589, 3, 342);
INSERT INTO `sys_role_menu` VALUES (11590, 3, 343);
INSERT INTO `sys_role_menu` VALUES (11591, 3, 344);
INSERT INTO `sys_role_menu` VALUES (11592, 3, 345);
INSERT INTO `sys_role_menu` VALUES (11593, 3, 346);
INSERT INTO `sys_role_menu` VALUES (11594, 3, 347);
INSERT INTO `sys_role_menu` VALUES (11595, 3, 348);
INSERT INTO `sys_role_menu` VALUES (11596, 3, 349);
INSERT INTO `sys_role_menu` VALUES (11597, 3, 350);
INSERT INTO `sys_role_menu` VALUES (11598, 3, 351);
INSERT INTO `sys_role_menu` VALUES (11599, 3, 352);
INSERT INTO `sys_role_menu` VALUES (11600, 3, 353);
INSERT INTO `sys_role_menu` VALUES (11601, 3, 354);
INSERT INTO `sys_role_menu` VALUES (11602, 3, 355);
INSERT INTO `sys_role_menu` VALUES (11603, 3, 371);
INSERT INTO `sys_role_menu` VALUES (11604, 3, 356);
INSERT INTO `sys_role_menu` VALUES (11605, 3, 357);
INSERT INTO `sys_role_menu` VALUES (11606, 3, 358);
INSERT INTO `sys_role_menu` VALUES (11607, 3, 359);
INSERT INTO `sys_role_menu` VALUES (11608, 3, 360);
INSERT INTO `sys_role_menu` VALUES (11609, 3, 361);
INSERT INTO `sys_role_menu` VALUES (11610, 3, 362);
INSERT INTO `sys_role_menu` VALUES (11611, 3, 368);
INSERT INTO `sys_role_menu` VALUES (11612, 3, 369);
INSERT INTO `sys_role_menu` VALUES (11613, 3, 370);
INSERT INTO `sys_role_menu` VALUES (11614, 3, 377);
INSERT INTO `sys_role_menu` VALUES (11615, 3, 378);
INSERT INTO `sys_role_menu` VALUES (11616, 3, 382);
INSERT INTO `sys_role_menu` VALUES (11617, 3, 383);
INSERT INTO `sys_role_menu` VALUES (11618, 3, 384);
INSERT INTO `sys_role_menu` VALUES (11619, 3, 385);
INSERT INTO `sys_role_menu` VALUES (11620, 3, 386);
INSERT INTO `sys_role_menu` VALUES (11621, 3, 387);
INSERT INTO `sys_role_menu` VALUES (11622, 3, 388);
INSERT INTO `sys_role_menu` VALUES (11623, 3, 256);
INSERT INTO `sys_role_menu` VALUES (11624, 3, 257);
INSERT INTO `sys_role_menu` VALUES (11625, 3, 258);
INSERT INTO `sys_role_menu` VALUES (11626, 3, 259);
INSERT INTO `sys_role_menu` VALUES (11627, 3, 260);
INSERT INTO `sys_role_menu` VALUES (11628, 3, 380);
INSERT INTO `sys_role_menu` VALUES (11629, 3, 381);
INSERT INTO `sys_role_menu` VALUES (11630, 3, 39);
INSERT INTO `sys_role_menu` VALUES (11631, 3, 82);
INSERT INTO `sys_role_menu` VALUES (11632, 3, 83);
INSERT INTO `sys_role_menu` VALUES (11633, 3, 340);
INSERT INTO `sys_role_menu` VALUES (11634, 3, 2);
INSERT INTO `sys_role_menu` VALUES (11635, 3, 15);
INSERT INTO `sys_role_menu` VALUES (11636, 3, 16);
INSERT INTO `sys_role_menu` VALUES (11637, 3, 17);
INSERT INTO `sys_role_menu` VALUES (11638, 3, 18);
INSERT INTO `sys_role_menu` VALUES (11639, 3, 3);
INSERT INTO `sys_role_menu` VALUES (11640, 3, 19);
INSERT INTO `sys_role_menu` VALUES (11641, 3, 20);
INSERT INTO `sys_role_menu` VALUES (11642, 3, 21);
INSERT INTO `sys_role_menu` VALUES (11643, 3, 22);
INSERT INTO `sys_role_menu` VALUES (11644, 3, 4);
INSERT INTO `sys_role_menu` VALUES (11645, 3, 23);
INSERT INTO `sys_role_menu` VALUES (11646, 3, 24);
INSERT INTO `sys_role_menu` VALUES (11647, 3, 25);
INSERT INTO `sys_role_menu` VALUES (11648, 3, 26);
INSERT INTO `sys_role_menu` VALUES (11649, 3, -666666);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '管理端用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(0) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_type` int(0) NULL DEFAULT NULL COMMENT '用户类型 1管理员 2商家管理员',
  PRIMARY KEY (`user_id`, `username`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理端系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '42e1cede05676449435d577f382221959846fdbb86d16ed5df6794440bb36310', 'YzcmCZNvbXocrsz9dm8e', '1212121254@qq.com', '13612345678', 1, 1, '2016-11-11 11:11:11', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 206 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (10, 3, 3);
INSERT INTO `sys_user_role` VALUES (20, 15, 3);
INSERT INTO `sys_user_role` VALUES (21, 16, 3);
INSERT INTO `sys_user_role` VALUES (25, 17, 3);
INSERT INTO `sys_user_role` VALUES (26, 11, 4);
INSERT INTO `sys_user_role` VALUES (27, 11, 3);
INSERT INTO `sys_user_role` VALUES (29, 20, 4);
INSERT INTO `sys_user_role` VALUES (32, 21, 4);
INSERT INTO `sys_user_role` VALUES (34, 23, 5);
INSERT INTO `sys_user_role` VALUES (35, 26, 3);
INSERT INTO `sys_user_role` VALUES (36, 6, 3);
INSERT INTO `sys_user_role` VALUES (38, 28, 3);
INSERT INTO `sys_user_role` VALUES (41, 27, 3);
INSERT INTO `sys_user_role` VALUES (48, 33, 3);
INSERT INTO `sys_user_role` VALUES (54, 36, 3);
INSERT INTO `sys_user_role` VALUES (81, 41, 3);
INSERT INTO `sys_user_role` VALUES (86, 43, 3);
INSERT INTO `sys_user_role` VALUES (92, 19, 3);
INSERT INTO `sys_user_role` VALUES (111, 47, 12);
INSERT INTO `sys_user_role` VALUES (112, 48, 12);
INSERT INTO `sys_user_role` VALUES (113, 50, 12);
INSERT INTO `sys_user_role` VALUES (114, 51, 12);
INSERT INTO `sys_user_role` VALUES (115, 52, 12);
INSERT INTO `sys_user_role` VALUES (116, 53, 12);
INSERT INTO `sys_user_role` VALUES (117, 56, 12);
INSERT INTO `sys_user_role` VALUES (118, 58, 12);
INSERT INTO `sys_user_role` VALUES (119, 60, 12);
INSERT INTO `sys_user_role` VALUES (120, 62, 12);
INSERT INTO `sys_user_role` VALUES (121, 64, 12);
INSERT INTO `sys_user_role` VALUES (122, 65, 12);
INSERT INTO `sys_user_role` VALUES (123, 66, 12);
INSERT INTO `sys_user_role` VALUES (124, 68, 12);
INSERT INTO `sys_user_role` VALUES (126, 69, 12);
INSERT INTO `sys_user_role` VALUES (127, 70, 12);
INSERT INTO `sys_user_role` VALUES (128, 71, 12);
INSERT INTO `sys_user_role` VALUES (129, 72, 12);
INSERT INTO `sys_user_role` VALUES (130, 77, 12);
INSERT INTO `sys_user_role` VALUES (131, 79, 12);
INSERT INTO `sys_user_role` VALUES (132, 80, 12);
INSERT INTO `sys_user_role` VALUES (133, 81, 12);
INSERT INTO `sys_user_role` VALUES (134, 85, 12);
INSERT INTO `sys_user_role` VALUES (135, 86, 12);
INSERT INTO `sys_user_role` VALUES (137, 88, 12);
INSERT INTO `sys_user_role` VALUES (138, 98, 12);
INSERT INTO `sys_user_role` VALUES (139, 101, 12);
INSERT INTO `sys_user_role` VALUES (140, 102, 12);
INSERT INTO `sys_user_role` VALUES (141, 104, 12);
INSERT INTO `sys_user_role` VALUES (142, 105, 12);
INSERT INTO `sys_user_role` VALUES (143, 107, 12);
INSERT INTO `sys_user_role` VALUES (144, 108, 12);
INSERT INTO `sys_user_role` VALUES (145, 109, 12);
INSERT INTO `sys_user_role` VALUES (147, 111, 3);
INSERT INTO `sys_user_role` VALUES (148, 34, 3);
INSERT INTO `sys_user_role` VALUES (150, 32, 3);
INSERT INTO `sys_user_role` VALUES (152, 31, 3);
INSERT INTO `sys_user_role` VALUES (154, 39, 3);
INSERT INTO `sys_user_role` VALUES (155, 40, 3);
INSERT INTO `sys_user_role` VALUES (156, 42, 3);
INSERT INTO `sys_user_role` VALUES (157, 112, 12);
INSERT INTO `sys_user_role` VALUES (158, 1, 3);
INSERT INTO `sys_user_role` VALUES (161, 115, 12);
INSERT INTO `sys_user_role` VALUES (162, 118, 12);
INSERT INTO `sys_user_role` VALUES (163, 119, 12);
INSERT INTO `sys_user_role` VALUES (164, 120, 12);
INSERT INTO `sys_user_role` VALUES (165, 121, 12);
INSERT INTO `sys_user_role` VALUES (166, 122, 12);
INSERT INTO `sys_user_role` VALUES (167, 123, 12);
INSERT INTO `sys_user_role` VALUES (168, 124, 12);
INSERT INTO `sys_user_role` VALUES (169, 125, 12);
INSERT INTO `sys_user_role` VALUES (170, 128, 12);
INSERT INTO `sys_user_role` VALUES (171, 129, 3);
INSERT INTO `sys_user_role` VALUES (173, 131, 12);
INSERT INTO `sys_user_role` VALUES (174, 134, 12);
INSERT INTO `sys_user_role` VALUES (175, 135, 12);
INSERT INTO `sys_user_role` VALUES (176, 138, 12);
INSERT INTO `sys_user_role` VALUES (177, 139, 12);
INSERT INTO `sys_user_role` VALUES (178, 140, 12);
INSERT INTO `sys_user_role` VALUES (179, 141, 12);
INSERT INTO `sys_user_role` VALUES (180, 142, 12);
INSERT INTO `sys_user_role` VALUES (181, 143, 12);
INSERT INTO `sys_user_role` VALUES (182, 144, 12);
INSERT INTO `sys_user_role` VALUES (183, 145, 3);
INSERT INTO `sys_user_role` VALUES (184, 145, 12);
INSERT INTO `sys_user_role` VALUES (185, 130, 3);
INSERT INTO `sys_user_role` VALUES (186, 130, 12);
INSERT INTO `sys_user_role` VALUES (187, 147, 12);
INSERT INTO `sys_user_role` VALUES (188, 148, 12);
INSERT INTO `sys_user_role` VALUES (189, 149, 12);
INSERT INTO `sys_user_role` VALUES (190, 150, 12);
INSERT INTO `sys_user_role` VALUES (191, 154, 12);
INSERT INTO `sys_user_role` VALUES (192, 155, 12);
INSERT INTO `sys_user_role` VALUES (193, 156, 12);
INSERT INTO `sys_user_role` VALUES (194, 159, 12);
INSERT INTO `sys_user_role` VALUES (195, 160, 12);
INSERT INTO `sys_user_role` VALUES (196, 161, 12);
INSERT INTO `sys_user_role` VALUES (197, 162, 12);
INSERT INTO `sys_user_role` VALUES (198, 163, 12);
INSERT INTO `sys_user_role` VALUES (199, 22, 4);
INSERT INTO `sys_user_role` VALUES (200, 164, 12);
INSERT INTO `sys_user_role` VALUES (201, 165, 12);
INSERT INTO `sys_user_role` VALUES (202, 35, 3);
INSERT INTO `sys_user_role` VALUES (203, 36, 3);
INSERT INTO `sys_user_role` VALUES (204, 37, 3);
INSERT INTO `sys_user_role` VALUES (205, 38, 3);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(0) NOT NULL COMMENT '管理端用户id',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理端系统用户Token表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (31, 'adcb863d41c396455c2cf7a282408b4a', '2023-04-28 23:08:03', '2023-04-28 11:08:03');

-- ----------------------------
-- Table structure for tb_coupon
-- ----------------------------
DROP TABLE IF EXISTS `tb_coupon`;
CREATE TABLE `tb_coupon`  (
  `coupon_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券图片',
  `valid_days` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效期天数',
  `min_money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券可使用订单最低金额',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券抵扣金额',
  `delete_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除 0未删除 -1已删除',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '是否启用 0未启用 1已启用',
  `coupon_type` int(0) NULL DEFAULT NULL COMMENT '优惠券类型 0全部可用 1酒店 2民宿',
  `max_receive` int(0) NULL DEFAULT NULL COMMENT '最多领取或购买数量(0为不限制数量)',
  `coupon_num` int(0) NULL DEFAULT NULL COMMENT '优惠券数量',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_coupon
-- ----------------------------
INSERT INTO `tb_coupon` VALUES (18, NULL, NULL, NULL, NULL, NULL, -1, NULL, NULL, NULL, NULL);
INSERT INTO `tb_coupon` VALUES (19, NULL, NULL, NULL, NULL, NULL, -1, NULL, NULL, NULL, NULL);
INSERT INTO `tb_coupon` VALUES (20, '优惠券满100减8元', NULL, '10', '100', 8.00, 0, 1, 1, 1, 1);
INSERT INTO `tb_coupon` VALUES (21, '酒店优惠劵满100减10', NULL, '3', '100', 10.00, 0, 1, 1, 2, 98);

-- ----------------------------
-- Table structure for tb_coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_coupon_user`;
CREATE TABLE `tb_coupon_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户优惠券id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券图片',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '优惠券领取时间',
  `employ_time` datetime(0) NULL DEFAULT NULL COMMENT '优惠券使用时间',
  `expiration_time` datetime(0) NULL DEFAULT NULL COMMENT '优惠券过期时间',
  `min_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券可使用订单最低金额',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `status` int(0) NULL DEFAULT NULL COMMENT '优惠券状态 0正常  1已使用  2已失效',
  `valid_days` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效天数',
  `coupon_id` int(0) NULL DEFAULT NULL COMMENT '绑定的优惠券id',
  `coupon_type` int(0) NULL DEFAULT NULL COMMENT '优惠券类型 0全部可用  1酒店 2民宿',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2629 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_coupon_user
-- ----------------------------
INSERT INTO `tb_coupon_user` VALUES (2621, 27, '优惠券1', NULL, '2026-07-02 18:32:35', NULL, '2026-07-12 18:32:35', 50.00, 20.00, 2, '10', 20, 1);
INSERT INTO `tb_coupon_user` VALUES (2622, 24, '优惠券1', NULL, '2026-07-20 11:22:30', NULL, '2026-07-30 11:22:30', 50.00, 20.00, 2, '10', 20, 1);
INSERT INTO `tb_coupon_user` VALUES (2623, 29, '酒店优惠劵满100减10', NULL, '2026-07-27 17:22:11', NULL, '2026-07-30 17:22:11', 100.00, 10.00, 2, '3', 21, 1);
INSERT INTO `tb_coupon_user` VALUES (2624, 31, '酒店优惠劵满100减10', NULL, '2026-07-28 09:59:22', NULL, '2026-07-31 09:59:22', 100.00, 10.00, 2, '3', 21, 1);
INSERT INTO `tb_coupon_user` VALUES (2625, 31, '酒店优惠劵满100减10', NULL, '2026-07-31 10:24:59', NULL, '2026-08-03 10:24:59', 100.00, 10.00, 2, '3', 21, 1);
INSERT INTO `tb_coupon_user` VALUES (2626, 32, '优惠券满100减8元', NULL, '2026-08-03 16:47:28', NULL, '2026-08-13 16:47:28', 100.00, 8.00, 1, '10', 20, 1);
INSERT INTO `tb_coupon_user` VALUES (2627, 32, '优惠券满100减8元', NULL, '2026-08-03 17:09:38', NULL, '2026-08-13 17:09:38', 100.00, 8.00, 1, '10', 20, 1);
INSERT INTO `tb_coupon_user` VALUES (2628, 32, '优惠券满100减8元', NULL, '2026-08-04 16:40:53', NULL, '2026-08-14 16:40:53', 100.00, 8.00, 1, '10', 20, 1);

-- ----------------------------
-- Table structure for tb_footprint
-- ----------------------------
DROP TABLE IF EXISTS `tb_footprint`;
CREATE TABLE `tb_footprint`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '酒店id',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1600 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户浏览足迹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_footprint
-- ----------------------------
INSERT INTO `tb_footprint` VALUES (1, 24, 2, '2026-05-13 14:38:38');
INSERT INTO `tb_footprint` VALUES (2, 24, 2, '2026-05-13 14:39:03');
INSERT INTO `tb_footprint` VALUES (3, 24, 2, '2026-05-13 14:46:42');
INSERT INTO `tb_footprint` VALUES (4, 24, 2, '2026-05-13 14:46:52');
INSERT INTO `tb_footprint` VALUES (5, 24, 2, '2026-05-13 14:47:46');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` int(0) NULL DEFAULT NULL COMMENT '性别 1男 2女',
  `age` int(0) NULL DEFAULT NULL COMMENT '年龄',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序openId',
  `app_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信app openId',
  `account_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信公众号 openId',
  `rider_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工小程序openId',
  `rider_app_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工微信app openId',
  `rider_account_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工微信公众号 openId',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益(可提现)',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密盐',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(0) NULL DEFAULT 1 COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `clientid_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户设备id',
  `rider_clientid_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工设备id',
  `sys_phone` int(0) NULL DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `apple_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果id',
  `cash_qr_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '微信提现二维码',
  `zhi_fu_bao_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝名字',
  `zhi_fu_bao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `user_type` int(0) NULL DEFAULT NULL COMMENT '1用户 2员工',
  `invitation_code` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人邀请码',
  `inviter_inviter_code` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人的上级邀请码',
  `zhi_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '直属佣金',
  `fei_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '非直属佣金',
  `is_vip` int(0) NULL DEFAULT 0 COMMENT '是否是会员 0否1是',
  `vip_end_time` datetime(0) NULL DEFAULT NULL COMMENT '会员到期时间',
  `is_shop_rider` int(0) NULL DEFAULT 2 COMMENT '是否是酒店员工：1是 2否 3商户老板',
  `shop_id` int(0) NULL DEFAULT NULL COMMENT '店铺id',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

-- ----------------------------
-- Table structure for user_and_info
-- ----------------------------
DROP TABLE IF EXISTS `user_and_info`;
CREATE TABLE `user_and_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户常用信息id',
  `info_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常用信息姓名',
  `info_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常用信息电话',
  `is_default` int(0) NULL DEFAULT 2 COMMENT '是否默认：1是 2否',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户常用信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_and_info
-- ----------------------------

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `money` decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '钱包金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_delete`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户钱包表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money
-- ----------------------------

-- ----------------------------
-- Table structure for user_money_details
-- ----------------------------
DROP TABLE IF EXISTS `user_money_details`;
CREATE TABLE `user_money_details`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `type` int(0) NULL DEFAULT NULL COMMENT '类别 1充值  2支出',
  `classify` int(0) NULL DEFAULT NULL COMMENT '途径 1充值 2订单支付 3分成 4开通会员',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `state` int(0) NULL DEFAULT NULL COMMENT '状态 1待支付 2已到账 3取消',
  `relation_id` int(0) NULL DEFAULT NULL COMMENT '关联id',
  `user_type` int(0) NULL DEFAULT NULL COMMENT '消息类型 1用户端 2员工端',
  `user_level` int(0) NULL DEFAULT NULL COMMENT '下级等级(1:一级用户 2:二级用户)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_delete_money_details`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户钱包明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money_details
-- ----------------------------

-- ----------------------------
-- Table structure for vip_config
-- ----------------------------
DROP TABLE IF EXISTS `vip_config`;
CREATE TABLE `vip_config`  (
  `vip_id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'vipId',
  `vip_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'vip标题',
  `vip_money` decimal(10, 2) NULL DEFAULT NULL COMMENT 'vip价格',
  `month_count` int(0) NULL DEFAULT NULL COMMENT '月份',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '是否开启0否 1是',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'vip配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_config
-- ----------------------------
INSERT INTO `vip_config` VALUES (9, '月会员', 0.01, 1, 1, 1, '2026-06-29 15:13:24', '2026-07-21 13:59:54');
INSERT INTO `vip_config` VALUES (11, '季会员', 39.90, 3, 1, 2, '2026-07-21 10:56:37', '2026-07-21 13:59:55');

SET FOREIGN_KEY_CHECKS = 1;
