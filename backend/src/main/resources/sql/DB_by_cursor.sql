-- �������ݿ�
CREATE DATABASE IF NOT EXISTS gogopet;

USE gogopet;

-- �û���
CREATE TABLE t_user (
    user_id VARCHAR(18) PRIMARY KEY COMMENT '�û�ID��Ψһ��ʶ��',
    user_name VARCHAR(63) NOT NULL COMMENT '�û��������ڵ�¼����ʾ',
    password VARCHAR(63) NOT NULL COMMENT '�û����룬���ܴ洢',
    user_type CHAR(1) NOT NULL COMMENT '�û����ͣ�U-��ͨ�û���C-���˹�˾��A-����Ա',
    user_address VARCHAR(63) COMMENT '�û���ַ���������˷���',
    picture_id VARCHAR(18) COMMENT '�û�ͷ��ID������ͼƬ��',
    company_id VARCHAR(18) COMMENT '��˾ID��������˾���������˹�˾�û���ֵ',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
    INDEX idx_user_name (user_name),
    INDEX idx_company_id (company_id)
);

-- ���˹�˾��
CREATE TABLE t_company (
    company_id VARCHAR(18) PRIMARY KEY COMMENT '��˾ID��Ψһ��ʶ��',
    company_intro TEXT COMMENT '��˾���ܣ���ϸ������˾���',
    company_local VARCHAR(63) COMMENT '��˾��ַ���������˷���',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��'
);

-- �����
CREATE TABLE t_pet (
    pet_id VARCHAR(18) PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ��������',
    pet_name VARCHAR(63) NOT NULL COMMENT '��������',
    pet_breed VARCHAR(63) COMMENT '����Ʒ��',
    pet_weight DECIMAL(5,2) COMMENT '�������أ���λ��ǧ��',
    pet_health_status VARCHAR(255) COMMENT '���｡��״������',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id)
);

-- ������
CREATE TABLE t_order (
    order_id VARCHAR(18) PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
    pet_id VARCHAR(18) NOT NULL COMMENT '����ID�����������',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ����������',
    company_id VARCHAR(18) NOT NULL COMMENT '��˾ID��������˾������ʾ�н����˵Ĺ�˾',
    order_status CHAR(1) NOT NULL COMMENT '����״̬��P-��֧����W-���ӵ���T-�����У�C-����ɣ�X-��ȡ��',
    pet_status CHAR(1) NOT NULL COMMENT '����״̬��N-������A-�쳣',
    delivery_status CHAR(1) NOT NULL COMMENT '����״̬��P-���ӵ���T-�����У�D-���ʹ�',
    start_time DATETIME COMMENT '���˿�ʼʱ��',
    end_time DATETIME COMMENT '���˽���ʱ��',
    start_location VARCHAR(255) COMMENT '��ʼ�ص�',
    end_location VARCHAR(255) COMMENT 'Ŀ�ĵ�',
    price DECIMAL(10,2) NOT NULL COMMENT '�����۸񣬵�λ��Ԫ',
    payment_status CHAR(1) NOT NULL COMMENT '֧��״̬��U-δ֧����P-��֧����R-���˿�',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (pet_id) REFERENCES t_pet(pet_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    FOREIGN KEY (company_id) REFERENCES t_company(company_id),
    INDEX idx_user_id (user_id),
    INDEX idx_company_id (company_id),
    INDEX idx_order_status (order_status)
);

-- ͼƬ��
CREATE TABLE t_picture (
    picture_id VARCHAR(18) PRIMARY KEY COMMENT 'ͼƬID��Ψһ��ʶ��',
    picture_usage VARCHAR(63) NOT NULL COMMENT 'ͼƬ��;��A-ͷ��P-������Ƭ��O-������Ƭ',
    picture_url VARCHAR(255) NOT NULL COMMENT 'ͼƬURL���洢ͼƬ�ķ���·��',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '�ϴ�ʱ��',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ�ϴ���',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_picture_usage (picture_usage)
);

-- ���ӱ�
CREATE TABLE t_post (
    post_id VARCHAR(18) PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ������',
    post_content TEXT NOT NULL COMMENT '��������',
    post_comment INT DEFAULT 0 COMMENT '��������',
    post_title VARCHAR(100) NOT NULL COMMENT '帖子标题',
    post_likes INT DEFAULT 0 COMMENT '点赞数',
    post_status TINYINT DEFAULT 1 COMMENT '帖子状态：0-已删除，1-正常',
    post_images TEXT COMMENT '帖子图片URL列表，以逗号分隔',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'ʱ',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id)
);

-- ۱
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

-- ϵͳ־
CREATE TABLE t_system_log (
    log_id VARCHAR(18) PRIMARY KEY COMMENT '��־ID��Ψһ��ʶ��',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ������',
    operation_type VARCHAR(63) NOT NULL COMMENT '�������ͣ����¼���޸���Ϣ��',
    operation_content TEXT COMMENT '�������ݣ���ϸ����������Ϣ',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_operation_time (operation_time)
);

-- ����׷�ٱ�
CREATE TABLE t_order_tracking (
    tracking_id VARCHAR(18) PRIMARY KEY COMMENT '׷��ID��Ψһ��ʶ��',
    order_id VARCHAR(18) NOT NULL COMMENT '����ID������������',
    location VARCHAR(255) NOT NULL COMMENT '��ǰλ��',
    status CHAR(1) NOT NULL COMMENT '״̬��T-�����У�R-��Ϣ��',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (order_id) REFERENCES t_order(order_id),
    INDEX idx_order_id (order_id),
    INDEX idx_create_time (create_time)
);

-- ��Ϣ֪ͨ��
CREATE TABLE t_notification (
    notification_id VARCHAR(18) PRIMARY KEY COMMENT '֪ͨID��Ψһ��ʶ��',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ������',
    notification_type VARCHAR(63) NOT NULL COMMENT '֪ͨ���ͣ�O-������S-ϵͳ',
    notification_content TEXT NOT NULL COMMENT '֪ͨ����',
    is_read BOOLEAN DEFAULT FALSE COMMENT '�Ƿ��Ѷ���0-δ����1-�Ѷ�',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read)
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

-- 修改帖子表，添加新字段
ALTER TABLE t_post
    ADD COLUMN post_title VARCHAR(100) NOT NULL COMMENT '帖子标题' AFTER user_id,
    ADD COLUMN post_likes INT DEFAULT 0 COMMENT '点赞数' AFTER post_content,
    ADD COLUMN post_status TINYINT DEFAULT 1 COMMENT '帖子状态：0-已删除，1-正常' AFTER post_comment;

-- 创建帖子图片表
CREATE TABLE IF NOT EXISTS t_post_image (
                                            image_id VARCHAR(32) PRIMARY KEY COMMENT '图片ID',
                                            post_id VARCHAR(32) NOT NULL COMMENT '帖子ID',
                                            image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
                                            image_order INT DEFAULT 0 COMMENT '图片顺序',
                                            create_time DATETIME NOT NULL COMMENT '创建时间',
                                            FOREIGN KEY (post_id) REFERENCES t_post(post_id)
) COMMENT '帖子图片表';

-- 创建点赞表
CREATE TABLE IF NOT EXISTS t_post_like (
                                           like_id VARCHAR(32) PRIMARY KEY COMMENT '点赞ID',
                                           post_id VARCHAR(32) NOT NULL COMMENT '帖子ID',
                                           user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
                                           create_time DATETIME NOT NULL COMMENT '创建时间',
                                           UNIQUE KEY uk_post_user (post_id, user_id),
                                           FOREIGN KEY (post_id) REFERENCES t_post(post_id),
                                           FOREIGN KEY (user_id) REFERENCES t_user(user_id)
) COMMENT '帖子点赞表';

/*-- 修改评论表
ALTER TABLE t_comment
    DROP COLUMN company_id,
    DROP COLUMN rating,
    ADD COLUMN post_id VARCHAR(32) NOT NULL COMMENT '帖子ID' AFTER comment_id,
    ADD COLUMN parent_id VARCHAR(32) DEFAULT NULL COMMENT '父评论ID' AFTER user_id,
    ADD COLUMN comment_status TINYINT DEFAULT 1 COMMENT '评论状态：0-已删除，1-正常' AFTER comment_content;

-- 1. 首先删除外键约束
ALTER TABLE t_comment
    DROP FOREIGN KEY t_comment_ibfk_1;

-- 2. 然后删除字段
ALTER TABLE t_comment
    DROP COLUMN company_id,
    DROP COLUMN rating;

-- 3. 添加新字段
ALTER TABLE t_comment
    ADD COLUMN post_id VARCHAR(32) NOT NULL COMMENT '帖子ID' AFTER comment_id,
    ADD COLUMN parent_id VARCHAR(32) DEFAULT NULL COMMENT '父评论ID' AFTER user_id,
    ADD COLUMN comment_status TINYINT DEFAULT 1 COMMENT '评论状态：0-已删除，1-正常' AFTER comment_content;

-- 4. 添加新的外键约束
ALTER TABLE t_comment
    ADD CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES t_post(post_id);
*/
-- 修改帖子表的主键长度
ALTER TABLE t_post
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 修改评论表的外键长度
ALTER TABLE t_comment
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 修改评论表的主键长度
ALTER TABLE t_comment
    MODIFY COLUMN comment_id VARCHAR(18) NOT NULL COMMENT '评论ID';

-- 修改评论表的其他字段长度
ALTER TABLE t_comment
    MODIFY COLUMN user_id VARCHAR(18) NOT NULL COMMENT '用户ID',
    MODIFY COLUMN parent_id VARCHAR(18) DEFAULT NULL COMMENT '父评论ID';

-- 修改帖子图片表的外键长度
ALTER TABLE t_post_image
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 修改点赞表的外键长度
ALTER TABLE t_post_like
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 删除旧的评论表
DROP TABLE IF EXISTS t_comment;

-- 创建新的评论表
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

-- 删除外键约束


ALTER TABLE t_comment
    DROP FOREIGN KEY t_comment_ibfk_1;

-- 修改帖子表的主键长度
ALTER TABLE t_post
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 修改评论表的外键长度
ALTER TABLE t_comment
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 修改评论表的主键长度
ALTER TABLE t_comment
    MODIFY COLUMN comment_id VARCHAR(18) NOT NULL COMMENT '评论ID';

-- 修改评论表的其他字段长度
ALTER TABLE t_comment
    MODIFY COLUMN user_id VARCHAR(18) NOT NULL COMMENT '用户ID',
    MODIFY COLUMN parent_id VARCHAR(18) DEFAULT NULL COMMENT '父评论ID';

-- 修改帖子图片表的外键长度
ALTER TABLE t_post_image
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 修改点赞表的外键长度
ALTER TABLE t_post_like
    MODIFY COLUMN post_id VARCHAR(18) NOT NULL COMMENT '帖子ID';

-- 重新添加外键约束
ALTER TABLE t_post_like
    ADD CONSTRAINT t_post_like_ibfk_1 FOREIGN KEY (post_id) REFERENCES t_post(post_id);

ALTER TABLE t_post_image
    ADD CONSTRAINT t_post_image_ibfk_1 FOREIGN KEY (post_id) REFERENCES t_post(post_id);

ALTER TABLE t_comment
    ADD CONSTRAINT t_comment_ibfk_1 FOREIGN KEY (post_id) REFERENCES t_post(post_id);
