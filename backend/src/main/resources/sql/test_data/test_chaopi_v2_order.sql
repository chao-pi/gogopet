-- 插入用户数据
INSERT INTO t_user (user_id, user_name, password, user_type, user_address, company_id) VALUES
('U001', '张三', '123', 'U', '北京市海淀区中关村大街1号', NULL),
('U002', '李四', '123', 'U', '上海市浦东新区陆家嘴', NULL),
('U003', '王五', '123', 'C', '广州市天河区珠江新城', 'C001'),
('U004', '赵六', '123', 'C', '深圳市南山区科技园', 'C002'),
('U005', '管理员', 'admin123', 'A', '北京市朝阳区', NULL);

-- 插入公司数据
INSERT INTO t_company (company_id, company_intro, company_local, rating, rating_count, service_area, transport_price_per_km, transport_count) VALUES
('C001', '专业宠物托运公司，提供全国范围内的宠物运输服务，拥有专业的运输团队和设备。', '广州市天河区珠江新城', 4.5, 120, 'D', 0.60, 150),
('C002', '国际宠物托运专家，提供国内外宠物运输服务，拥有丰富的国际运输经验。', '深圳市南山区科技园', 4.8, 85, 'I', 0.80, 200),
('C003', '省内宠物托运服务，专注省内宠物运输，提供快速安全的运输服务。', '广州市越秀区', 4.2, 65, 'P', 0.45, 80);

-- 插入宠物数据
INSERT INTO t_pet (pet_id, user_id, pet_name, pet_breed, pet_weight, pet_age, pet_gender, pet_health_status) VALUES
('PET001', 'U001', '旺财', '金毛', 25.5, 3, 'M', '健康，已接种疫苗'),
('PET002', 'U001', '咪咪', '布偶猫', 4.2, 2, 'F', '健康，已绝育'),
('PET003', 'U001', '豆豆', '柯基', 12.3, 1, 'M', '健康，已接种疫苗'),
('PET004', 'U002', '花花', '波斯猫', 3.8, 4, 'F', '健康，已绝育'),
('PET005', 'U002', '小黑', '拉布拉多', 28.0, 2, 'M', '健康，已接种疫苗');