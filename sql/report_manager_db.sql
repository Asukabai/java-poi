/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50727
Source Host           : 127.0.0.1:3306
Source Database       : report_manager_db

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2020-03-21 22:06:35
*/

CREATE DATABASE /*!32312 IF NOT EXISTS*/`report_manager_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `report_manager_db`;


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept` (
  `id` bigint(20) DEFAULT NULL COMMENT '部门编号',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '部门编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_dept
-- ----------------------------
INSERT INTO `tb_dept` VALUES ('5', '资产管理部');
INSERT INTO `tb_dept` VALUES ('6', '质量监察部');
INSERT INTO `tb_dept` VALUES ('7', '营销部');
INSERT INTO `tb_dept` VALUES ('1', '销售部');
INSERT INTO `tb_dept` VALUES ('2', '人事部');
INSERT INTO `tb_dept` VALUES ('3', '财务部');
INSERT INTO `tb_dept` VALUES ('4', '技术部');

-- ----------------------------
-- Table structure for tb_province
-- ----------------------------
DROP TABLE IF EXISTS `tb_province`;
CREATE TABLE `tb_province` (
  `id` bigint(50) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '省份或直辖市或特别行政区名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_province
-- ----------------------------
INSERT INTO `tb_province` VALUES ('1', '北京市');
INSERT INTO `tb_province` VALUES ('2', '天津市');
INSERT INTO `tb_province` VALUES ('3', '上海市');
INSERT INTO `tb_province` VALUES ('4', '重庆市');
INSERT INTO `tb_province` VALUES ('5', '河北省');
INSERT INTO `tb_province` VALUES ('6', '山西省');
INSERT INTO `tb_province` VALUES ('7', '辽宁省');
INSERT INTO `tb_province` VALUES ('8', '吉林省');
INSERT INTO `tb_province` VALUES ('9', '黑龙江省');
INSERT INTO `tb_province` VALUES ('10', '江苏省');
INSERT INTO `tb_province` VALUES ('11', '浙江省');
INSERT INTO `tb_province` VALUES ('12', '安徽省');
INSERT INTO `tb_province` VALUES ('13', '福建省');
INSERT INTO `tb_province` VALUES ('14', '江西省');
INSERT INTO `tb_province` VALUES ('15', '山东省');
INSERT INTO `tb_province` VALUES ('16', '河南省');
INSERT INTO `tb_province` VALUES ('17', '湖北省');
INSERT INTO `tb_province` VALUES ('18', '湖南省');
INSERT INTO `tb_province` VALUES ('19', '广东省');
INSERT INTO `tb_province` VALUES ('20', '海南省');
INSERT INTO `tb_province` VALUES ('21', '四川省');
INSERT INTO `tb_province` VALUES ('22', '贵州省');
INSERT INTO `tb_province` VALUES ('23', '云南省');
INSERT INTO `tb_province` VALUES ('24', '陕西省');
INSERT INTO `tb_province` VALUES ('25', '甘肃省');
INSERT INTO `tb_province` VALUES ('26', '青海省');
INSERT INTO `tb_province` VALUES ('27', '台湾省');
INSERT INTO `tb_province` VALUES ('28', '内蒙古自治区');
INSERT INTO `tb_province` VALUES ('29', '广西壮族自治区');
INSERT INTO `tb_province` VALUES ('30', '西藏自治区');
INSERT INTO `tb_province` VALUES ('31', '宁夏回族自治区');
INSERT INTO `tb_province` VALUES ('32', '新疆维吾尔自治区');
INSERT INTO `tb_province` VALUES ('33', '香港特别行政区');
INSERT INTO `tb_province` VALUES ('34', '澳门特别行政区');

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` double(10,1) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `need_return` tinyint(1) DEFAULT NULL COMMENT '是否需要归还',
  `photo` varchar(200) DEFAULT NULL COMMENT '照片',
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES ('1', '记录本', '2.0', '3', '0', '\\resource_photos\\3\\1.jpg');
INSERT INTO `tb_resource` VALUES ('2', '笔记本电脑', '7000.0', '3', '1', '\\resource_photos\\3\\2.jpg');
INSERT INTO `tb_resource` VALUES ('3', '办公桌', '1000.0', '3', '1', '\\resource_photos\\3\\3.jpg');
INSERT INTO `tb_resource` VALUES ('4', '订书机', '50.0', '4', '1', '\\resource_photos\\4\\1.jpg');
INSERT INTO `tb_resource` VALUES ('5', '双面胶带', '5.0', '4', '0', '\\resource_photos\\4\\2.jpg');
INSERT INTO `tb_resource` VALUES ('6', '资料文件夹', '10.0', '4', '0', '\\resource_photos\\4\\3.jpg');
INSERT INTO `tb_resource` VALUES ('7', '打印机', '1200.0', '4', '1', '\\resource_photos\\4\\4.jpg');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机号',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `salary` int(10) DEFAULT NULL,
  `hire_date` datetime DEFAULT NULL COMMENT '入职日期',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门编号',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `photo` varchar(200) DEFAULT NULL COMMENT '照片路径',
  `address` varchar(300) DEFAULT NULL COMMENT '现在住址',
  PRIMARY KEY (`id`),
  KEY `fk_dept` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', '大一', '13800000001', '北京市', '北京市', '11000', '2001-01-01 21:18:29', '1', '1981-03-02 00:00:00', '\\static\\user_photos\\1.jpg', '北京市西城区宣武大街1号院');
INSERT INTO `tb_user` VALUES ('2', '不二', '13800000002', '河北省', '石家庄市', '12000', '2002-01-02 21:18:29', '2', '1982-03-02 00:00:00', '\\static\\user_photos\\2.jpg', '北京市西城区宣武大街2号院');
INSERT INTO `tb_user` VALUES ('3', '张三', '13800000003', '河北省', '石家庄市', '13000', '2003-03-03 21:18:29', '3', '1983-03-02 00:00:00', '\\static\\user_photos\\3.jpg', '北京市西城区宣武大街3号院');
INSERT INTO `tb_user` VALUES ('4', '李四', '13800000004', '河北省', '石家庄市', '14000', '2004-02-04 21:18:29', '4', '1984-03-02 00:00:00', '\\static\\user_photos\\4.jpg', '北京市西城区宣武大街4号院');
INSERT INTO `tb_user` VALUES ('5', '王五', '13800000005', '河北省', '唐山市', '15000', '2005-03-05 21:18:29', '5', '1985-03-02 00:00:00', '\\static\\user_photos\\5.jpg', '北京市西城区宣武大街5号院');
INSERT INTO `tb_user` VALUES ('6', '赵六', '13800000006', '河北省', '承德市省', '16000', '2006-04-06 21:18:29', '6', '1986-03-02 00:00:00', '\\static\\user_photos\\6.jpg', '北京市西城区宣武大街6号院');
INSERT INTO `tb_user` VALUES ('7', '沈七', '13800000007', '河北省', '秦皇岛市', '17000', '2007-06-07 21:18:29', '7', '1987-03-02 00:00:00', '\\static\\user_photos\\7.jpg', '北京市西城区宣武大街7号院');
INSERT INTO `tb_user` VALUES ('8', '酒八', '13800000008', '河北省', '秦皇岛市', '18000', '2008-07-08 21:18:29', '6', '1988-03-02 00:00:00', '\\static\\user_photos\\8.jpg', '北京市西城区宣武大街8号院');
INSERT INTO `tb_user` VALUES ('9', '第九', '13800000009', '山东省', '德州市', '19000', '2009-03-09 21:18:29', '1', '1989-03-02 00:00:00', '\\static\\user_photos\\9.jpg', '北京市西城区宣武大街9号院');
INSERT INTO `tb_user` VALUES ('10', '石十', '13800000010', '山东省', '青岛市', '20000', '2010-07-10 21:18:29', '4', '1990-03-02 00:00:00', '\\static\\user_photos\\10.jpg', '北京市西城区宣武大街10号院');
INSERT INTO `tb_user` VALUES ('11', '肖十一', '13800000011', '山东省', '青岛市', '21000', '2011-12-11 21:18:29', '4', '1991-03-02 00:00:00', '\\static\\user_photos\\11.jpg', '北京市西城区宣武大街11号院');
INSERT INTO `tb_user` VALUES ('12', '星十二', '13800000012', '山东省', '青岛市', '22000', '2012-05-12 21:18:29', '4', '1992-03-02 00:00:00', '\\static\\user_photos\\12.jpg', '北京市西城区宣武大街12号院');
INSERT INTO `tb_user` VALUES ('13', '钗十三', '13800000013', '山东省', '济南市', '23000', '2013-06-13 21:18:29', '3', '1993-03-02 00:00:00', '\\static\\user_photos\\13.jpg', '北京市西城区宣武大街13号院');
INSERT INTO `tb_user` VALUES ('14', '贾十四', '13800000014', '山东省', '威海市', '24000', '2014-06-14 21:18:29', '2', '1994-03-02 00:00:00', '\\static\\user_photos\\14.jpg', '北京市西城区宣武大街14号院');
INSERT INTO `tb_user` VALUES ('15', '甄世武', '13800000015', '山东省', '济南市', '25000', '2015-06-15 21:18:29', '4', '1995-03-02 00:00:00', '\\static\\user_photos\\15.jpg', '北京市西城区宣武大街15号院');


-- ----------------------------
-- Table structure for tb_month
-- ----------------------------
DROP TABLE IF EXISTS `tb_month`;
CREATE TABLE `tb_month` (
  `name` varchar(2) DEFAULT NULL COMMENT '月份'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_month
-- ----------------------------
INSERT INTO `tb_month` VALUES ('01');
INSERT INTO `tb_month` VALUES ('02');
INSERT INTO `tb_month` VALUES ('03');
INSERT INTO `tb_month` VALUES ('04');
INSERT INTO `tb_month` VALUES ('05');
INSERT INTO `tb_month` VALUES ('06');
INSERT INTO `tb_month` VALUES ('07');
INSERT INTO `tb_month` VALUES ('08');
INSERT INTO `tb_month` VALUES ('09');
INSERT INTO `tb_month` VALUES ('10');
INSERT INTO `tb_month` VALUES ('11');
INSERT INTO `tb_month` VALUES ('12');