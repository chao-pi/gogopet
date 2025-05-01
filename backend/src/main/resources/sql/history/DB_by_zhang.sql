create schema PetGoGO;
use PetGoGO;

CREATE TABLE t_user (
                        user_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '用户ID，唯一标识符',
                        user_name VARCHAR(63) NOT NULL COMMENT '用户名，注册信息',
                        password VARCHAR(63) NOT NULL COMMENT '密码，注册信息',
                        user_type CHAR(1) NOT NULL COMMENT '用户身份，类型（用户/托运司机/管理员/……）',
                        user_address VARCHAR(63) NULL COMMENT '用户地址，限于用户、司机填写',
                        picture_id VARCHAR(18) NULL COMMENT '用户头像，外键，指明用户头像',
                        company_id VARCHAR(18) NULL COMMENT '公司ID，外键，如果身份为司机则指明公司',
                        FOREIGN KEY (picture_id) REFERENCES t_picture(picture_id),
                        FOREIGN KEY (company_id) REFERENCES t_company(company_id)
);

CREATE TABLE t_pet (
                       pet_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '宠物ID，唯一标识符',
                       user_id VARCHAR(18) NOT NULL COMMENT '用户ID，外键，指明其主人',
                       pet_name VARCHAR(63) NOT NULL COMMENT '宠物名称',
                       pet_breed VARCHAR(63) NULL COMMENT '宠物品种',
                       pet_weight VARCHAR(18) NULL COMMENT '宠物体重',
                       pet_health_status CHAR(1) NULL COMMENT '宠物健康状态',
                       FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

CREATE TABLE t_picture (
                           picture_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '图片ID，唯一标识符',
                           picture_usage CHAR(1) NOT NULL COMMENT '图片用途，包括（推文/图像识别/……）'
);

CREATE TABLE t_order (
                         order_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '订单ID，唯一标识符',
                         pet_id VARCHAR(18) NOT NULL COMMENT '宠物ID，外键，指明该订单涉及的宠物',
                         picture_id VARCHAR(18) NOT NULL COMMENT '图片ID，外键，指明该订单包含的宠物图片',
                         order_status CHAR(1) NOT NULL COMMENT '订单状态（未指派/托运中/已完成/已取消/……）',
                         pet_status CHAR(1) NOT NULL COMMENT '托运状态（健康/生病/……）',
                         delivery_status CHAR(1) NOT NULL COMMENT '运输状态，指明当前托运司机的运输进度',
                         FOREIGN KEY (pet_id) REFERENCES t_pet(pet_id),
                         FOREIGN KEY (picture_id) REFERENCES t_picture(picture_id)
);

CREATE TABLE t_chat (
                        chat_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '对话ID，唯一标识符',
                        user_id VARCHAR(18) NOT NULL COMMENT '用户ID，外键，指明该会话主体为哪个客户',
                        chat_content LONGTEXT NULL COMMENT '储存会话',
                        FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

CREATE TABLE t_post (
                        post_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '推文ID，唯一标识符',
                        user_id VARCHAR(18) NOT NULL COMMENT '用户ID，外键，指明当前推文的发布者',
                        post_content LONGTEXT NULL COMMENT '推文内容',
                        post_comment LONGTEXT NULL COMMENT '推文评论',
                        FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

CREATE TABLE t_company (
                           company_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '公司ID，唯一标识符',
                           company_intro LONGTEXT NOT NULL COMMENT '公司介绍',
                           company_local VARCHAR(63) NOT NULL COMMENT '公司地址'
);

CREATE TABLE t_comment (
                           comment_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '评价ID，唯一标识符',
                           company_id VARCHAR(18) NOT NULL COMMENT '公司ID，外键，指明评论的对象',
                           user_id VARCHAR(18) NOT NULL COMMENT '用户ID，外键，指明评论的发布者',
                           comment_content LONGTEXT NULL COMMENT '评论内容',
                           FOREIGN KEY (company_id) REFERENCES t_company(company_id),
                           FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);