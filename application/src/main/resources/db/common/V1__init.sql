-------------------------------------------------------------------------------------
-- DB Schema
-------------------------------------------------------------------------------------

-- User Schema
CREATE TABLE `tb_user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `login_id`    VARCHAR(300) NULL,
    `username`    VARCHAR(255) NULL,
    `password`    VARCHAR(255) NULL,
    `type`        VARCHAR(128) NULL,
    `use_yn`      CHAR(1)      DEFAULT 'N' NULL,
    `create_date` TIMESTAMP    NULL,
    `modify_date` TIMESTAMP    NULL,
    `creator`     BIGINT       NULL,
    `modifier`    BIGINT       NULL
);
COMMENT ON TABLE `tb_user`                IS '사용자테이블';
COMMENT ON COLUMN `tb_user`.`id`          IS '사용자식별자';
COMMENT ON COLUMN `tb_user`.`login_id`    IS '사용자로그인ID';
COMMENT ON COLUMN `tb_user`.`username`    IS '사용자사용자명';
COMMENT ON COLUMN `tb_user`.`password`    IS '패스워드';
COMMENT ON COLUMN `tb_user`.`type`        IS '사용자종류()';
COMMENT ON COLUMN `tb_user`.`use_yn`      IS '사용여부(Y,N)';
COMMENT ON COLUMN `tb_user`.`create_date` IS '생성일시';
COMMENT ON COLUMN `tb_user`.`modify_date` IS '수정일시';
COMMENT ON COLUMN `tb_user`.`creator`     IS '생성자';
COMMENT ON COLUMN `tb_user`.`modifier`    IS '수정자';

--ALTER TABLE `tb_user` ADD UNIQUE INDEX `login_id_UNIQUE` (`login_id` ASC);

CREATE TABLE `tb_authority` (
    `user_id`     BIGINT        NOT NULL,
    `authority`   VARCHAR(128)  NOT NULL
);

ALTER TABLE `tb_authority` ADD CONSTRAINT `FK_tb_user_TO_tb_authority_1` FOREIGN KEY (
    `user_id`
)
REFERENCES `tb_user` (
    `id`
);

-- Group Authorities

CREATE TABLE `tb_group` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `group_name`  VARCHAR(255)  NOT NULL
);

CREATE TABLE `tb_group_authority` (
    `group_id`    BIGINT        NOT NULL,
    `authority`   VARCHAR(128)  NOT NULL
);

ALTER TABLE `tb_group_authority` ADD CONSTRAINT `FK_tb_group_TO_tb_group_authority_1` FOREIGN KEY (
    `group_id`
)
REFERENCES `tb_group` (
    `id`
);


CREATE TABLE `tm_group_user` (
    `user_id`     BIGINT        NOT NULL,
    `group_id`    BIGINT        NOT NULL
);

ALTER TABLE `tm_group_user` ADD CONSTRAINT `FK_tb_group_TO_tm_group_user_1` FOREIGN KEY (
    `group_id`
)
REFERENCES `tb_group` (
    `id`
);

ALTER TABLE `tm_group_user` ADD CONSTRAINT `FK_tb_user_TO_tm_group_user_1` FOREIGN KEY (
    `user_id`
)
REFERENCES `tb_user` (
    `id`
);

-- Persistent Login Schema

--create table persistent_logins (
--    username VARCHAR(64) not null,
--    series VARCHAR(64) primary key,
--    token VARCHAR(64) not null,
--    last_used TIMESTAMP not null
--);

-- ACL Schema
-- https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/domain-acls.html

--CREATE TABLE acl_sid (
--    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    principal BOOLEAN NOT NULL,
--    sid VARCHAR(100) NOT NULL,
--    UNIQUE KEY unique_acl_sid (sid, principal)
--) ENGINE=InnoDB;
--
--CREATE TABLE acl_class (
--    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    class VARCHAR(100) NOT NULL,
--    UNIQUE KEY uk_acl_class (class)
--) ENGINE=InnoDB;
--
--CREATE TABLE acl_object_identity (
--    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    object_id_class BIGINT UNSIGNED NOT NULL,
--    object_id_identity BIGINT NOT NULL,
--    parent_object BIGINT UNSIGNED,
--    owner_sid BIGINT UNSIGNED,
--    entries_inheriting BOOLEAN NOT NULL,
--    UNIQUE KEY uk_acl_object_identity (object_id_class, object_id_identity),
--    CONSTRAINT fk_acl_object_identity_parent FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
--    CONSTRAINT fk_acl_object_identity_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
--    CONSTRAINT fk_acl_object_identity_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
--) ENGINE=InnoDB;
--
--CREATE TABLE acl_entry (
--    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    acl_object_identity BIGINT UNSIGNED NOT NULL,
--    ace_order INTEGER NOT NULL,
--    sid BIGINT UNSIGNED NOT NULL,
--    mask INTEGER UNSIGNED NOT NULL,
--    granting BOOLEAN NOT NULL,
--    audit_success BOOLEAN NOT NULL,
--    audit_failure BOOLEAN NOT NULL,
--    UNIQUE KEY unique_acl_entry (acl_object_identity, ace_order),
--    CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),
--    CONSTRAINT fk_acl_entry_acl FOREIGN KEY (sid) REFERENCES acl_sid (id)
--) ENGINE=InnoDB;

--CREATE TABLE `tb_user_prop` (
--   `id`            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
--   `image_file_id` BIGINT       NULL,
--   `birthday`      DATE         NULL,
--   `email`         VARCHAR(255) NULL,
--   `gender`        CHAR(1)      NULL,
--   `create_date`   TIMESTAMP    NULL,
--   `modify_date`   TIMESTAMP    NULL,
--   `creator`       BIGINT       NULL,
--   `modifier`      BIGINT       NULL
--);
--COMMENT ON TABLE `tb_user_prop`                IS '사용자속성테이블';
--COMMENT ON COLUMN `tb_user_prop`.`id`          IS '사용자속성식별자';

CREATE TABLE `tc_file` (
   `id`                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `original_filename` VARCHAR(255) NULL,
   `filename`          VARCHAR(255) NULL,
   `extension`         CHAR(8)      NULL,
   `type`              VARCHAR(128) NULL,
   `size`              INT(11)      NULL,
   `create_date`       TIMESTAMP    NULL,
   `creator`           BIGINT       NULL
);

COMMENT ON TABLE `tc_file`                IS '파일테이블';
COMMENT ON COLUMN `tc_file`.`id`          IS '파일식별자';
COMMENT ON COLUMN `tc_file`.`original_filename` IS '원본파일명';
COMMENT ON COLUMN `tc_file`.`filename`    IS '저장된파일명';
COMMENT ON COLUMN `tc_file`.`extension`   IS '확장자';
COMMENT ON COLUMN `tc_file`.`type`        IS '파일종류(MIME type)';
COMMENT ON COLUMN `tc_file`.`size`        IS '파일크가(byte)';
COMMENT ON COLUMN `tc_file`.`create_date` IS '생성일시';
COMMENT ON COLUMN `tc_file`.`creator`     IS '생성자';

CREATE TABLE `tc_code` (
   `code`        CHAR(8)      NOT NULL PRIMARY KEY,
   `group_code`  CHAR(4)      NOT NULL,
   `name`        VARCHAR(255) NULL,
   `order`       INT          NULL,
   `create_date` TIMESTAMP    NULL,
   `modify_date` TIMESTAMP    NULL,
   `creator`     BIGINT       NULL,
   `modifier`    BIGINT       NULL
);

CREATE TABLE `tc_code_group` (
   `code`        CHAR(4)      NOT NULL PRIMARY KEY,
   `name`        VARCHAR(255) NULL,
   `create_date` TIMESTAMP    NULL,
   `modify_date` TIMESTAMP    NULL,
   `creator`     BIGINT       NULL,
   `modifier`    BIGINT       NULL
);

-------------------------------------------------------------------------------------
-- DB Data
-------------------------------------------------------------------------------------

-- 계정 --
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(10, 'admin', '관리자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(11, 'oper1', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(12, 'oper2', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(13, 'oper3', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(14, 'oper4', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);    
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(15, 'oper5', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(16, 'oper6', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(17, 'oper7', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(18, 'oper8', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(19, 'oper9', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);
INSERT INTO tb_user(id, login_id, username, password, use_yn, create_date, modify_date, creator, modifier) 
    VALUES(20, 'oper10', '운영자', '$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC', 'Y', NOW(), NOW(), 10, 10);

INSERT INTO `tb_authority`(`user_id`, `authority`) VALUES(10, 'ROLE_ADMIN');
INSERT INTO `tb_authority`(`user_id`, `authority`) VALUES(10, 'ROLE_OPER');
INSERT INTO `tb_authority`(`user_id`, `authority`) VALUES(11, 'ROLE_OPER');
INSERT INTO `tb_authority`(`user_id`, `authority`) VALUES(12, 'ROLE_OPER');

INSERT INTO `tb_group`(`id`, `group_name`) VALUES(1, 'FOO');
INSERT INTO `tb_group_authority`(`group_id`, `authority`) VALUES(1, 'ROLE_ADMIN2');
INSERT INTO `tb_group_authority`(`group_id`, `authority`) VALUES(1, 'ROLE_OPER2');
INSERT INTO `tm_group_user`(`user_id`, `group_id`) VALUES(10, 1);

-- 그룹 코드 --
insert into tc_code_group(code, name, create_date, modify_date, creator, modifier)  values('ROLE', '권한', now(), now(), 10, 10);

-- 상세 코드 --
insert into tc_code(code, group_code, name, create_date, modify_date, creator, modifier) values('ROLEADMN', 'ROLE', '관리자', now(), now(), 10, 10);
insert into tc_code(code, group_code, name, create_date, modify_date, creator, modifier) values('ROLEOPER', 'ROLE', '운영자', now(), now(), 10, 10);
