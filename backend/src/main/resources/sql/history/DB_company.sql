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
    transport_methods CHAR(1) DEFAULT 'L' COMMENT '运输方式，每个字符代表一种方式：A-空运，L-陆运，S-海运',
    service_area CHAR(1) DEFAULT 'D' COMMENT '服务区域：P-省内，D-国内，I-国际',
    transport_price_per_km DECIMAL(10,2) DEFAULT 0.50 COMMENT '每公里价格',
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

-- 订单表
CREATE TABLE t_order (
    order_id VARCHAR(18) PRIMARY KEY COMMENT '订单ID，唯一标识符',
    pet_id VARCHAR(18) NOT NULL COMMENT '宠物ID，关联宠物表',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示下单用户',
    company_id VARCHAR(18) NOT NULL COMMENT '公司ID，关联公司表，表示接单的公司',
    order_status CHAR(1) NOT NULL COMMENT '订单状态，P-待支付，W-待接单，T-进行中，C-已完成，X-已取消',
    pet_status CHAR(1) NOT NULL COMMENT '宠物状态，N-正常，A-异常',
    delivery_status CHAR(1) NOT NULL COMMENT '配送状态，P-待接单，T-进行中，D-已送达',
    start_time DATETIME COMMENT '寄养开始时间',
    end_time DATETIME COMMENT '寄养结束时间',
    start_location VARCHAR(255) COMMENT '起始地点',
    end_location VARCHAR(255) COMMENT '目的地',
    price DECIMAL(10,2) NOT NULL COMMENT '订单价格，单位：元',
    payment_status CHAR(1) NOT NULL COMMENT '支付状态，U-未支付，P-已支付，R-已退款',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (pet_id) REFERENCES t_pet(pet_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    FOREIGN KEY (company_id) REFERENCES t_company(company_id),
    INDEX idx_user_id (user_id),
    INDEX idx_company_id (company_id),
    INDEX idx_order_status (order_status)
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
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id)
);

-- 评论表
CREATE TABLE t_comment (
    comment_id VARCHAR(18) PRIMARY KEY COMMENT '评论ID，唯一标识符',
    company_id VARCHAR(18) NOT NULL COMMENT '公司ID，关联公司表，表示被评论的公司',
    user_id VARCHAR(18) NOT NULL COMMENT '用户ID，关联用户表，表示评论人',
    comment_content TEXT NOT NULL COMMENT '评论内容',
    rating TINYINT NOT NULL COMMENT '评分，1-5分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (company_id) REFERENCES t_company(company_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_company_id (company_id),
    INDEX idx_user_id (user_id)
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

-- 订单追踪表
CREATE TABLE t_order_tracking (
    tracking_id VARCHAR(18) PRIMARY KEY COMMENT '追踪ID，唯一标识符',
    order_id VARCHAR(18) NOT NULL COMMENT '订单ID，关联订单表',
    location VARCHAR(255) NOT NULL COMMENT '当前位置',
    status CHAR(1) NOT NULL COMMENT '状态，T-运输中，R-休息中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
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