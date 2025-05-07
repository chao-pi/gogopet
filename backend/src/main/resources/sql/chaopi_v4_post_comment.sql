-- 创建数据库
CREATE DATABASE IF NOT EXISTS gogopet;

USE gogopet;

-- 用户表
CREATE TABLE t_user (
    user_id VARCHAR(18) PRIMARY KEY COMMENT '用户ID，唯一标识符',
    user_name VARCHAR(63) NOT NULL COMMENT '用户名，用于登录和显示',
    password VARCHAR(63) NOT NULL COMMENT '用户密码，加密存储',
    user_type CHAR(1) NOT NULL COMMENT '用户类型，U-普通用户，C-宠物公司，A-管理员',
    user_address VARCHAR(63) COMMENT '用户地址，用于配送服务',
    picture_id VARCHAR(18) COMMENT '用户头像ID，关联图片表',
    company_id VARCHAR(18) COMMENT '公司ID，仅宠物公司用户有值',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_name (user_name),
    INDEX idx_company_id (company_id)
);

-- 宠物公司表
CREATE TABLE t_company (
    company_id VARCHAR(18) PRIMARY KEY COMMENT '公司ID，唯一标识符',
    company_intro TEXT COMMENT '公司介绍，详细描述公司情况',
    company_local VARCHAR(63) COMMENT '公司地址，用于配送服务',
    rating DECIMAL(2,1) DEFAULT 3.0 COMMENT '公司平均评分',
    rating_count INT DEFAULT 0 COMMENT '评分总数',
    service_area CHAR(1) DEFAULT 'D' COMMENT '服务区域：P-省内，D-国内，I-国际',
    transport_price_per_km DECIMAL(10,2) DEFAULT 0.50 COMMENT '每公里价格',
    transport_count INT DEFAULT 0 COMMENT '公司完成的托运订单数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_rating (rating)
);

-- 宠物表
CREATE TABLE t_pet (
    pet_id VARCHAR(18) PRIMARY KEY COMMENT '宠物ID，唯一标识符',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示宠物主人',
    pet_name VARCHAR(63) NOT NULL COMMENT '宠物名称',
    pet_breed VARCHAR(63) COMMENT '宠物品种',
    pet_weight DECIMAL(5,2) COMMENT '宠物体重，单位：千克',
    pet_age INT COMMENT '宠物年龄，单位：岁',
    pet_gender CHAR(1) COMMENT '宠物性别，M-雄性，F-雌性',
    pet_health_status VARCHAR(255) COMMENT '宠物健康状况描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id)
);

CREATE TABLE t_order (
    order_id VARCHAR(18) PRIMARY KEY COMMENT '订单ID，唯一标识符',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示下单用户',
    company_id VARCHAR(18) NOT NULL COMMENT '公司ID，关联公司表，表示接单的公司',
    order_status CHAR(1) NOT NULL COMMENT '订单状态：P-待支付，W-待接单，T-进行中，C-已完成，X-已取消',
    pet_status CHAR(1) NOT NULL COMMENT '宠物状态：N-正常，A-异常',
    transport_method VARCHAR(20) NOT NULL COMMENT '运输方式：SPECIAL-专车托运，SHARE-拼车托运，AIR-空运托运',
    
    -- 地址信息
    start_province VARCHAR(20) COMMENT '起始省份',
    start_city VARCHAR(20) COMMENT '起始城市',
    start_district VARCHAR(20) COMMENT '起始区县',
    start_location VARCHAR(255) COMMENT '起始详细地址',
    start_latitude DECIMAL(10,6) COMMENT '起始地纬度',
    start_longitude DECIMAL(10,6) COMMENT '起始地经度',
    
    end_province VARCHAR(20) COMMENT '目的省份',
    end_city VARCHAR(20) COMMENT '目的城市',
    end_district VARCHAR(20) COMMENT '目的区县',
    end_location VARCHAR(255) COMMENT '目的详细地址',
    end_latitude DECIMAL(10,6) COMMENT '目的地纬度',
    end_longitude DECIMAL(10,6) COMMENT '目的地经度',
    
    -- 时间信息
    start_time DATETIME COMMENT '托运开始时间',
    end_time DATETIME COMMENT '托运结束时间',
    complete_time DATETIME COMMENT '订单完成时间',
    
    -- 订单信息
    distance DECIMAL(10,2) COMMENT '运输距离，单位：公里',
    price DECIMAL(10,2) NOT NULL COMMENT '订单价格，单位：元',
    order_remark TEXT COMMENT '订单备注，用户特殊要求',
    rating DECIMAL(2,1) COMMENT '用户评分，1-5分',
    rating_comment TEXT COMMENT '用户评价内容',
    
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    FOREIGN KEY (company_id) REFERENCES t_company(company_id),
    
    INDEX idx_user_id (user_id),
    INDEX idx_company_id (company_id),
    INDEX idx_order_status (order_status)
);

-- 新增订单宠物关联表
CREATE TABLE t_order_pet (
    order_pet_id VARCHAR(18) PRIMARY KEY COMMENT '订单宠物关联ID',
    order_id VARCHAR(18) NOT NULL COMMENT '订单ID',
    pet_id VARCHAR(18) NOT NULL COMMENT '宠物ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    FOREIGN KEY (order_id) REFERENCES t_order(order_id),
    FOREIGN KEY (pet_id) REFERENCES t_pet(pet_id),
    
    INDEX idx_order_id (order_id),
    INDEX idx_pet_id (pet_id)
);

-- 图片表
CREATE TABLE t_picture (
    picture_id VARCHAR(18) PRIMARY KEY COMMENT '图片ID，唯一标识符',
    picture_usage VARCHAR(63) NOT NULL COMMENT '图片用途，A-头像，P-宠物照片，O-其他照片',
    picture_url VARCHAR(255) NOT NULL COMMENT '图片URL，存储图片的服务路径',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示上传者',
    pet_id VARCHAR(18) COMMENT '宠物ID，关联宠物表，表示所属宠物',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    FOREIGN KEY (pet_id) REFERENCES t_pet(pet_id),
    INDEX idx_user_id (user_id),
    INDEX idx_pet_id (pet_id),
    INDEX idx_picture_usage (picture_usage)
);

-- 帖子表
CREATE TABLE t_post (
    post_id VARCHAR(18) PRIMARY KEY COMMENT '帖子ID，唯一标识符',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示发帖人',
    post_content TEXT NOT NULL COMMENT '帖子内容',
    post_comment INT DEFAULT 0 COMMENT '评论数量',
    post_title VARCHAR(100) NOT NULL COMMENT '帖子标题',
    post_likes INT DEFAULT 0 COMMENT '点赞数',
    post_status TINYINT DEFAULT 1 COMMENT '帖子状态：0-已删除，1-正常',
    post_images TEXT COMMENT '帖子图片URL列表，以逗号分隔',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id)
);

-- 订单追踪表
CREATE TABLE t_order_tracking (
    tracking_id VARCHAR(18) PRIMARY KEY COMMENT '追踪ID，唯一标识符',
    order_id VARCHAR(18) NOT NULL COMMENT '订单ID，关联订单表',
    location VARCHAR(255) NOT NULL COMMENT '当前位置',
    latitude DECIMAL(10,6) COMMENT '纬度',
    longitude DECIMAL(10,6) COMMENT '经度',
    status CHAR(2) NOT NULL COMMENT '状态：T1-在起点，T2-第一检查点，T3-第二检查点，T4-第三检查点，T5-到达终点，R-休息中，L-装卸中，D-已送达',
    remark TEXT COMMENT '备注信息，如异常情况说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    estimated_arrival_time DATETIME COMMENT '预计到达时间，仅供参考',
    update_frequency INT DEFAULT 30 COMMENT '更新时间间隔（分钟）',
    last_update_time DATETIME COMMENT '最后更新时间',
    
    FOREIGN KEY (order_id) REFERENCES t_order(order_id),
    INDEX idx_order_id (order_id),
    INDEX idx_create_time (create_time)
);

-- 消息通知表
CREATE TABLE t_notification (
    notification_id VARCHAR(18) PRIMARY KEY COMMENT '通知ID，唯一标识符',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示接收者',
    notification_type VARCHAR(63) NOT NULL COMMENT '通知类型，O-订单，S-系统',
    notification_content TEXT NOT NULL COMMENT '通知内容',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读，0-未读，1-已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read)
);

-- 系统日志表
CREATE TABLE t_system_log (
    log_id VARCHAR(18) PRIMARY KEY COMMENT '日志ID，唯一标识符',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示操作人',
    operation_type VARCHAR(63) NOT NULL COMMENT '操作类型，如登录、修改信息等',
    operation_content TEXT COMMENT '操作内容，详细记录操作信息',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_operation_time (operation_time)
);

-- 智能助手聊天消息表
CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role VARCHAR(20) NOT NULL COMMENT '消息角色：user-用户消息，assistant-AI助手消息',
    content TEXT NOT NULL COMMENT '消息内容',
    create_time DATETIME NOT NULL COMMENT '消息创建时间',
    session_id VARCHAR(64) NOT NULL COMMENT '会话ID，用于区分不同用户的对话',
    INDEX idx_session_id (session_id) COMMENT '会话ID索引，用于优化查询性能'
) COMMENT='智能助手聊天消息表'; 

-- 评论表
CREATE TABLE t_comment (
    comment_id VARCHAR(18) PRIMARY KEY COMMENT '评论ID，唯一标识符',
    post_id VARCHAR(18) NOT NULL COMMENT '帖子ID',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联评论人',
    parent_id VARCHAR(18) DEFAULT NULL COMMENT '父评论ID，用于回复功能',
    comment_status TINYINT DEFAULT 1 COMMENT '评论状态：0-已删除，1-正常',
    comment_content TEXT NOT NULL COMMENT '评论内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (post_id) REFERENCES t_post(post_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id)
);

-- 帖子图片表
CREATE TABLE t_post_image (
    image_id VARCHAR(32) PRIMARY KEY COMMENT '图片ID',
    post_id VARCHAR(32) NOT NULL COMMENT '帖子ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
    image_order INT DEFAULT 0 COMMENT '图片顺序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    FOREIGN KEY (post_id) REFERENCES t_post(post_id)
) COMMENT '帖子图片表';

-- 点赞表
CREATE TABLE t_post_like (
    like_id VARCHAR(32) PRIMARY KEY COMMENT '点赞ID',
    post_id VARCHAR(32) NOT NULL COMMENT '帖子ID',
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    UNIQUE KEY uk_post_user (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES t_post(post_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id)
) COMMENT '帖子点赞表';
