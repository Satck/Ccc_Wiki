drop table if exists 'doc';
create table 'doc '(
    'id'  bigint not null comment 'id',
    'ebook_id' bigint not null default 0 comment '电子书id',
    'parent' bigint not null default 0 comment '父id',
    'name' varchar(50) not null comment '名称',
    'sort' int comment '顺序',
    'view_count' int default 0 comment '阅读数',
    'vote_count' int default 0 comment '点赞数',
    primary key ('id')
)engine=innodb default charset =utf8mb4 comment ='文档';

insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (1,1,0,'文档1',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (2,1,1,'文档1.1',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (3,1,0,'文档2',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (4,1,3,'文档1',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (5,1,3,'文档2',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (6,1,5,'文档1',1,0,0);


-- 文档内容
drop table if exists `content`;
create  table `content`(
    `id` bigint not null comment '文档id',
    `content` mediumtext not null comment '内容',
    primary key (`id`)
)engine=innodb default charset=utf8mb4 comment ='文档内容';

-- 用户表
drop table if exists `user`;
create table `user` (
    `id` bigint not null comment 'ID',
    `login_name` varchar(50) not null comment '登录名',
    `name` varchar(50) comment '昵称',
    `password` char(32) not null comment '密码',
    primary key (`id`),
    unique key `login_name_unique`(`login_name`)
) engine = innodb default charset = utf8mb4 comment ='用户';