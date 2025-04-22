create schema PetGoGO;
use PetGoGO;

CREATE TABLE t_user (
                        user_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '�û�ID��Ψһ��ʶ��',
                        user_name VARCHAR(63) NOT NULL COMMENT '�û�����ע����Ϣ',
                        password VARCHAR(63) NOT NULL COMMENT '���룬ע����Ϣ',
                        user_type CHAR(1) NOT NULL COMMENT '�û���ݣ����ͣ��û�/����˾��/����Ա/������',
                        user_address VARCHAR(63) NULL COMMENT '�û���ַ�������û���˾����д',
                        picture_id VARCHAR(18) NULL COMMENT '�û�ͷ�������ָ���û�ͷ��',
                        company_id VARCHAR(18) NULL COMMENT '��˾ID�������������Ϊ˾����ָ����˾',
                        FOREIGN KEY (picture_id) REFERENCES t_picture(picture_id),
                        FOREIGN KEY (company_id) REFERENCES t_company(company_id)
);

CREATE TABLE t_pet (
                       pet_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
                       user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������ָ��������',
                       pet_name VARCHAR(63) NOT NULL COMMENT '��������',
                       pet_breed VARCHAR(63) NULL COMMENT '����Ʒ��',
                       pet_weight VARCHAR(18) NULL COMMENT '��������',
                       pet_health_status CHAR(1) NULL COMMENT '���｡��״̬',
                       FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

CREATE TABLE t_picture (
                           picture_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT 'ͼƬID��Ψһ��ʶ��',
                           picture_usage CHAR(1) NOT NULL COMMENT 'ͼƬ��;������������/ͼ��ʶ��/������'
);

CREATE TABLE t_order (
                         order_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
                         pet_id VARCHAR(18) NOT NULL COMMENT '����ID�������ָ���ö����漰�ĳ���',
                         picture_id VARCHAR(18) NOT NULL COMMENT 'ͼƬID�������ָ���ö��������ĳ���ͼƬ',
                         order_status CHAR(1) NOT NULL COMMENT '����״̬��δָ��/������/�����/��ȡ��/������',
                         pet_status CHAR(1) NOT NULL COMMENT '����״̬������/����/������',
                         delivery_status CHAR(1) NOT NULL COMMENT '����״̬��ָ����ǰ����˾�����������',
                         FOREIGN KEY (pet_id) REFERENCES t_pet(pet_id),
                         FOREIGN KEY (picture_id) REFERENCES t_picture(picture_id)
);

CREATE TABLE t_chat (
                        chat_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '�Ի�ID��Ψһ��ʶ��',
                        user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������ָ���ûỰ����Ϊ�ĸ��ͻ�',
                        chat_content LONGTEXT NULL COMMENT '����Ự',
                        FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

CREATE TABLE t_post (
                        post_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
                        user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������ָ����ǰ���ĵķ�����',
                        post_content LONGTEXT NULL COMMENT '��������',
                        post_comment LONGTEXT NULL COMMENT '��������',
                        FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

CREATE TABLE t_company (
                           company_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '��˾ID��Ψһ��ʶ��',
                           company_intro LONGTEXT NOT NULL COMMENT '��˾����',
                           company_local VARCHAR(63) NOT NULL COMMENT '��˾��ַ'
);

CREATE TABLE t_comment (
                           comment_id VARCHAR(18) NOT NULL PRIMARY KEY COMMENT '����ID��Ψһ��ʶ��',
                           company_id VARCHAR(18) NOT NULL COMMENT '��˾ID�������ָ�����۵Ķ���',
                           user_id VARCHAR(18) NOT NULL COMMENT '�û�ID�������ָ�����۵ķ�����',
                           comment_content LONGTEXT NULL COMMENT '��������',
                           FOREIGN KEY (company_id) REFERENCES t_company(company_id),
                           FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);