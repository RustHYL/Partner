-- 标签表
create table tag
(
    id          bigint auto_increment comment 'id'
        primary key,
    tag_name    varchar(256)                       null comment '标签名称',
    user_id     bigint                             null comment '用户id',
    parent_id   bigint                             null comment '父标签id',
    is_parent   tinyint                            null comment '是否为标签，0是父标签，1不是',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete   tinyint  default 0                 not null comment '是否被删除',
    constraint uniIdx_tagName
        unique (tag_name)
)
    comment '标签';

create index idx_userId
    on tag (user_id);

-- 用户表
create table user
(
    username      varchar(256)                       null comment '用户昵称',
    id            bigint auto_increment comment 'id'
        primary key,
    user_account  varchar(256)                       null comment '账号',
    avatar_url    varchar(1024)                      null comment '用户头像',
    gender        tinyint                            null comment '性别',
    user_password varchar(512)                       not null comment '密码',
    phone         varchar(128)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    user_status   int      default 0                 null comment '状态 0表示正常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否被删除',
    user_role     int      default 0                 not null comment '用户角色 0普通用户 1管理员',
    verify_code   varchar(512)                       null comment '校验账号',
    tags          varchar(1024)                      null comment '标签列表'
)
    comment '用户';

-- 队伍表
create table team
(
    id            bigint auto_increment comment 'id'  primary key,
    name          varchar(256)                       not null comment '队伍名称',
    description   varchar(1024)                      null comment '队伍描述',
    max_num       int      default 1                 null comment '最大人数',
    expire_time   datetime                           null comment '过期时间',
    user_id       bigint                             null   comment '用户id',
    status    int      default 0                 not null comment '状态 0-表示公开，1-表示私有，2-表示加密',
    password      varchar(512)                       null comment '队伍密码',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否被删除'
)
    comment '队伍';

-- 用户队伍关系表
create table user_team
(
    id            bigint auto_increment comment 'id' primary key,
    user_id            bigint                       not null comment '用户id',
    team_id            bigint                       not null  comment '队伍id',
    join_time   datetime                   null comment '加入时间',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否被删除'
)
    comment '用户队伍关系' collate = utf8mb4_0900_ai_ci;
