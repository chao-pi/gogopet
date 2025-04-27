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
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_user_id (user_id)
);

-- ���۱�
CREATE TABLE t_comment (
    comment_id VARCHAR(18) PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
    company_id VARCHAR(18) NOT NULL COMMENT '��˾ID��������˾������ʾ�����۵Ĺ�˾',
    user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������û�������ʾ������',
    comment_content TEXT NOT NULL COMMENT '��������',
    rating TINYINT NOT NULL COMMENT '���֣�1-5��',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
    FOREIGN KEY (company_id) REFERENCES t_company(company_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id),
    INDEX idx_company_id (company_id),
    INDEX idx_user_id (user_id)
);

-- ϵͳ��־��
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
