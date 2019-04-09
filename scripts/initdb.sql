INSERT INTO `sys_dictionary_cate` VALUES (1, NULL, 1, NULL, 'ACCIDENT_TYPE_CODE', '事件形态', '事件形态');
INSERT INTO `sys_dictionary` VALUES (1, '2018-7-3 18:04:30', 1, '2018-7-6 09:54:33', '1234', '1111', 1);

INSERT INTO `third_party_certificate` VALUES (1, '2018-7-6 11:44:51', 1, '2018-7-6 11:44:51', 'TRAFFIC_QWEQWEQWE', 'ASDAQQWREQW!@$QAFDSFASF');

insert into `traffic`.`sys_conf` ( `remark`, `update_time`, `id`, `code`, `create_time`, `value`, `state`, `name`) values ( '每个事故大约等待时间', '2018-07-08 00:28:49', '6', 'SYS_WAIT_TIME', '2018-07-08 00:28:49', '15', '1', '每个事故等待时间');

INSERT INTO `sys_menu` VALUES (1, '2018-7-6 09:46:56', 1, '2018-7-6 09:47:01', 'fa-cog', '\0', '首页', NULL, 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '\0', '系统', NULL, 2, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '系统参数', NULL, 99, '/manage/sys_config.html', 2);
INSERT INTO `sys_menu` VALUES (4, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '系统字典', NULL, 100, '/manage/sys_dictionary.html', 2);
INSERT INTO `sys_menu` VALUES (6, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '操作员管理', NULL, 102, '/manage/sys_person.html', 2);
INSERT INTO `sys_menu` VALUES (13, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '\0', '快处中心', NULL, 3, NULL, NULL);
INSERT INTO `sys_menu` VALUES (7, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '天气', NULL, 103, '/manage/weather.html', 13);
INSERT INTO `sys_menu` VALUES (8, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '保险公司', NULL, 109, '/manage/insurance_company.html', 13);
INSERT INTO `sys_menu` VALUES (9, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '交通分局', NULL, 104, '/manage/coordination_center.html', 13);
INSERT INTO `sys_menu` VALUES (10, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '事故原因', NULL, 105, '/manage/accident_reason.html', 13);
INSERT INTO `sys_menu` VALUES (11, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '违法行为', NULL, 106, '/manage/illegal_behavior.html', 13);
INSERT INTO `sys_menu` VALUES (12, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '法律条款', NULL, 107, '/manage/clause.html', 13);
INSERT INTO `sys_menu` VALUES (14, '2018-7-6 09:46:56', 1, '2018-7-6 09:49:25', 'fa-cog', '', '待处理事故', NULL, 108, '/manage/PendingCoordinationAccident.html', 13);

INSERT INTO `sys_role` VALUES (1, NULL, 1, NULL, '超级管理员', NULL);

INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (1, 7);
INSERT INTO `sys_role_menu` VALUES (1, 9);
INSERT INTO `sys_role_menu` VALUES (1, 10);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (1, 13);
INSERT INTO `sys_role_menu` VALUES (1, 14);

INSERT INTO `sys_person` VALUES (1, '2018-7-6 09:50:49', 1, '2018-7-7 11:42:41', 1, 'admin', 1, 'upload\\sysperson\\admin\\2018070711423916403.png', 'admin', '202cb962ac59075b964b07152d234b70', '18990029999', NULL, '2020-10-1 09:50:15', null);

INSERT INTO `sys_person_role` VALUES (1, 1);