-- xiaoxiao 数据库创建
create schema xiaoxiao;

create table activity
(
    id                      int auto_increment
        primary key,
    user_id                 bigint                                             not null comment '创建活动的用户的id',
    creator_name            varchar(40) collate utf8_unicode_ci default 'test' not null comment '创建活动的用户的昵称',
    cheer_count             bigint(40) default 0 null comment '打气计数',
    activity_topic          varchar(255)                                       not null comment '活动标题',
    activity_form           int                                                not null comment '活动形式（0线上 1线下）',
    activity_NumberOfPeople bigint null comment '活动人数',
    activity_notice         varchar(255) collate utf8_unicode_ci null comment '活动须知',
    activity_content        varchar(1024) null comment '活动介绍',
    media_path              varchar(255) collate utf8_unicode_ci null comment '文件储存路径',
    is_delete               tinyint(1) default 0 null comment '是否删除',
    identity_id             bigint null comment '匿名id(',
    create_time             datetime                            default CURRENT_TIMESTAMP null,
    activity_time           datetime                                           not null,
    activity_local          varchar(255)                                       not null,
    activity_endtime        datetime                                           not null,
    is_cheer                tinyint(1) default 0 not null,
    constraint activity_id_uindex
        unique (id)
) comment '活动主体表';

create
fulltext index bbs_activity_ft_index
    on activity (activity_topic, activity_local, activity_content);

create table activity_comment
(
    id            bigint auto_increment
        primary key,
    activity_id   bigint                             not null comment '活动id',
    user_id       bigint                             not null comment '用户真实id',
    identity_id   bigint null comment '匿名身份id',
    creator_name  varchar(100)                       not null comment '创建者用户名',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    content       varchar(300)                       not null comment '评论内容',
    comment_count int      default 0                 not null comment '评论数',
    like_count    int      default 0                 not null comment '点赞数',
    media_path    varchar(500) null comment '媒体路径',
    is_delete     tinyint                            not null comment '是否被删除',
    constraint activity_comment_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table activity_comment_like
(
    id         bigint auto_increment
        primary key,
    user_id    bigint not null comment '用户真实id',
    comment_id bigint not null comment '评论id',
    status     tinyint(2) not null comment '状态，1为评论，2为子评论'
) collate = utf8_unicode_ci;

create table activity_sub_comment
(
    id           bigint auto_increment
        primary key,
    comment_id   bigint                             not null comment '父评论id',
    user_id      bigint                             not null comment '用户真实id',
    identity_id  bigint null comment '匿名身份id',
    creator_name varchar(100)                       not null comment '创建者用户名',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    content      varchar(300)                       not null comment '评论内容',
    reply_name   varchar(100) null comment '回复给谁',
    media_path   varchar(500) null comment '媒体路径',
    is_delete    tinyint                            not null comment '是否被删除',
    like_count   int      default 0                 not null comment '点赞数',
    constraint activity_sub_comment_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table admin_user
(
    id          int auto_increment
        primary key,
    user_name   varchar(20)   not null,
    password    varchar(20)   not null,
    create_time datetime      not null,
    is_delete   int default 0 not null
) comment '管理系统成员';

create table base_file
(
    id          int auto_increment
        primary key,
    file_name   varchar(255) not null,
    file_type   varchar(255) not null,
    url         varchar(255) not null,
    file_size   double null,
    state       varchar(255) null,
    file_hash   varchar(255) null,
    creator     varchar(255) null,
    create_time datetime null,
    updater     varchar(255) null,
    update_time datetime null on update CURRENT_TIMESTAMP,
    is_delete   int null,
    constraint file_name
        unique (file_name)
) collate = utf8_unicode_ci;

create table base_mail_log
(
    id             bigint auto_increment
        primary key,
    target_address varchar(200) null,
    send_content   varchar(5000) null,
    send_reason    varchar(5000) null,
    send_time      datetime null,
    send_result    int(4) null,
    constraint base_mail_log_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table bbs_association
(
    id          bigint auto_increment
        primary key,
    json_data   varchar(8192) null comment 'json内容',
    is_delete   tinyint(1) null comment '删除标志（0代表未删除，1代表已删除）',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    uid         bigint null comment '创建人id',
    constraint associations_id_uindex
        unique (id)
);

create table bbs_comment
(
    id                bigint auto_increment
        primary key,
    post_id           bigint        not null comment '帖子id',
    content           varchar(5000) not null comment '内容',
    user_id           bigint        not null comment '创建者id',
    identity_id       bigint(11) null comment '身份id',
    creator_name      varchar(100) collate utf8_unicode_ci null comment '创建者名',
    create_time       datetime null comment '创建时间',
    update_time       datetime null comment '更新时间
',
    updater           varchar(100) collate utf8_unicode_ci null comment '修改者',
    status            tinyint null comment '状态',
    like_count        int default 0 null comment '点赞数',
    sub_comment_count int default 0 null comment '子评论数',
    view_count        int default 0 null comment '浏览量',
    unlike_count      int default 0 null comment '踩数',
    is_delete         tinyint(1) default 0 null comment '是否被删除',
    media_path        varchar(1000) collate utf8_unicode_ci null comment '媒体存储路径',
    parent_id         bigint null comment '父评论id',
    root_comment_id   bigint null comment '根评论id',
    constraint bbs_comment_id_uindex
        unique (id)
);

create table bbs_comment_like
(
    id          bigint auto_increment
        primary key,
    user_id     bigint not null,
    identity_id bigint null,
    entity_id   bigint not null,
    entity_type tinyint(255) not null,
    status      tinyint(255) null
) collate = utf8_unicode_ci;

create table bbs_like
(
    id          bigint unsigned auto_increment
        primary key,
    uid         bigint null comment '用户id',
    content_id  bigint(255) null comment '点赞的内容id',
    type        int(4) null comment '内容类型，枚举表示',
    flag        tinyint null comment '标识：1点赞、2点踩、3删除',
    create_time timestamp null,
    update_time timestamp null
) collate = utf8_unicode_ci;

create index bbs_like_content_id_index
    on bbs_like (content_id);

create index bbs_like_uid_index
    on bbs_like (uid);

create table bbs_post
(
    id            bigint auto_increment
        primary key,
    title         varchar(100) null comment '标题',
    content       varchar(5000) null comment '内容',
    user_id       bigint null comment '创建者id',
    identity_id   bigint null comment '匿名身份id',
    creator_name  varchar(100) collate utf8_unicode_ci null comment '创建者名',
    create_time   datetime null comment '创建时间',
    update_time   datetime null comment '更新时间
',
    modifier_name varchar(100) collate utf8_unicode_ci null comment '修改者',
    status        tinyint null comment '状态',
    like_count    int null comment '点赞数',
    comment_count int null comment '评论数',
    share_count   int null comment '分享量',
    view_count    int null comment '浏览量',
    is_delete     tinyint default 0 null comment '是否被删除',
    media_path    varchar(4096) collate utf8_unicode_ci null comment '媒体存储路径',
    tag_id        bigint null comment '标签id',
    extend_data   varchar(512) null,
    constraint bbs_post_id_uindex
        unique (id)
);

create
fulltext index text_index
    on bbs_post (title, content);

create table bbs_report
(
    id                bigint unsigned auto_increment
        primary key,
    uid               int         not null comment '创建该举报的用户的id',
    report_type       varchar(20) not null comment '举报类别',
    target_content_id bigint null comment '举报目标内容的id',
    target_uid        bigint null comment '举报目标的用户id',
    target_content    varchar(4096) null comment '举报时的内容快照',
    reason            varchar(4096) null comment '举报理由',
    media_path        varchar(2048) null comment '举报图片',
    create_time       datetime null comment '举报时间',
    status            int null comment '状态'
) comment '投诉举报';

create table bbs_sub_comment
(
    id           bigint auto_increment
        primary key,
    comment_id   bigint       not null,
    content      varchar(255) not null,
    user_id      bigint       not null,
    identity_id  bigint null,
    creator_name varchar(255) not null,
    create_time  datetime null,
    update_time  datetime null,
    updater      int null,
    status       tinyint(255) null,
    like_count   int(255) default 0 null,
    unlike_count int(255) default 0 null,
    is_delete    tinyint(1) unsigned zerofill default 0 null,
    media_path   varchar(255) null,
    reply_name   varchar(255) null,
    view_count   int(255) default 0 null,
    reply_id     bigint null
) collate = utf8_unicode_ci;

create table bbs_topic
(
    id            bigint auto_increment
        primary key,
    creator_id    bigint null comment '创建者id',
    creator_name  varchar(100) null comment '创建者名',
    create_time   datetime null comment '创建时间',
    update_time   datetime null comment '更新时间
',
    updater       varchar(100) null comment '修改者',
    status        tinyint null comment '状态',
    like_count    int     default 0 null comment '点赞数',
    comment_count int     default 0 null comment '评论数',
    view_count    int     default 1 null comment '浏览量',
    unlike_count  int     default 0 null comment '踩数',
    is_delete     tinyint default 0 null comment '是否被删除',
    topic_name    varchar(255) null,
    identity_id   bigint(11) null,
    share_count   int(255) default 0 null,
    constraint bbs_topic_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table bbs_topic_post
(
    id        bigint auto_increment
        primary key,
    topic_id  bigint null,
    post_id   bigint null,
    is_delete tinyint(255) default 0 null,
    constraint topic_post_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table bbs_user_liked
(
    id          bigint unsigned auto_increment
        primary key,
    user_id     bigint null comment '用户id',
    content_id  bigint(255) null comment '具体内容id',
    identity_id tinyint(20) null comment '区分用户匿名',
    sign_id     tinyint null comment '区分评论/帖子/话题等等',
    status      tinyint null comment '区分点赞和点踩',
    create_time timestamp null,
    update_time timestamp null,
    constraint user_id
        unique (user_id, content_id, sign_id)
) collate = utf8_unicode_ci;

create table browsing_history
(
    id                  bigint auto_increment
        primary key,
    user_id             bigint not null comment '用户id',
    browsing_history_id bigint not null comment '浏览帖子id',
    browsing_time       bigint null comment '浏览时间',
    constraint browsing_history_pk_2
        unique (user_id, browsing_history_id)
) comment '浏览历史';

create table course_info
(
    id          int auto_increment
        primary key,
    student_id  varchar(20) null,
    term        varchar(20) null,
    detail      json null,
    note        json null,
    uid         varchar(32) null,
    wechat_id   varchar(32) null,
    device_id   varchar(32) null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    is_delete   tinyint   default 0 null comment '是否删除',
    is_send     int       default 0                 not null
) collate = utf8_unicode_ci;

create index index_name
    on course_info (term);

create table doc
(
    id          bigint auto_increment
        primary key,
    parent_id   bigint default -1 null comment '父节点id',
    doc_key     varchar(32) null comment '父节点id',
    title       varchar(32) null comment '标题',
    row_content longtext null comment '原始内容',
    md_content  longtext null comment 'MarkDown内容',
    view_count  int    default 0 null comment '浏览量',
    sort_weight int    default 1 null comment '排序权重 ',
    show_state  tinyint(1) null comment '可见状态0否、1是',
    is_delete   tinyint(1) null comment '删除标志（0代表未删除，1代表已删除）',
    creator     varchar(32) charset utf8mb4 null comment '更新者',
    create_time datetime null comment '创建时间',
    updater     varchar(32) charset utf8mb4 null comment '更新者',
    update_time datetime null comment '更新时间',
    constraint doc_id_uindex
        unique (id),
    constraint doc_key
        unique (doc_key)
);

create table free_room
(
    id               int auto_increment comment 'id'
        primary key,
    week             int         not null comment '周次',
    day              int null comment '星期',
    building_id      varchar(30) not null,
    course_time      varchar(10) null,
    classroom        varchar(10) not null comment '教室号',
    seat_number      int         not null,
    exam_seat_number int         not null
);

create index week
    on free_room (week);

create index week_day_buildingid
    on free_room (week, day, building_id, course_time);

create table im_message
(
    id           bigint auto_increment
        primary key,
    src_id       varchar(200) null comment '消息发送者id',
    dst_id       varchar(200) null comment '消息接收者id',
    message      text null comment '消息内容',
    create_time  datetime null,
    update_time  datetime null,
    message_type tinyint null,
    constraint im_message_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table inform_max_id
(
    id          bigint auto_increment
        primary key,
    user_id     varchar(500) null comment '用户id',
    read_max_id bigint null comment '用户已读最大id',
    create_time datetime null,
    update_time datetime null,
    inform_type int null comment '通知类型',
    constraint id
        unique (id),
    constraint inform_max_id_user_id_inform_type_uindex
        unique (user_id, inform_type)
) collate = utf8_unicode_ci;

create table product
(
    id          bigint auto_increment
        primary key,
    price       decimal null comment '价格',
    type        int null comment '商品类型',
    productData varchar(4096) null comment '商品',
    status      int null comment '状态',
    uid         bigint null comment '创建人id',
    createTime  datetime null comment '创建时间',
    constraint product_id_uindex
        unique (id)
);

create table reward_task
(
    id          bigint auto_increment
        primary key,
    uid         bigint null comment '用户id',
    content     varchar(1024) null comment '内容',
    media_path  varchar(4096) null comment '媒体资源路径逗号分隔',
    bounty      decimal(10, 2) null comment '赏金',
    contact     varchar(128) null comment '联系方式',
    status      tinyint null comment '状态',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    type        tinyint null comment '类型',
    constraint reward_task_id_uindex
        unique (id)
);

create table reward_task_comment
(
    id             bigint auto_increment
        primary key,
    parent_id      bigint null comment '父评论id',
    reward_task_id bigint null comment '悬赏任务id',
    uid            bigint null comment '用户id',
    identity_id    bigint null comment '匿名id',
    content        varchar(1024) null comment '内容',
    is_delete      tinyint null comment '是否删除',
    create_time    datetime null comment '创建时间',
    update_time    datetime null comment '更新时间',
    constraint reward_task_comment_id_uindex
        unique (id)
);

create table term_score
(
    id         int auto_increment
        primary key,
    userid     bigint        not null,
    score_json json          not null,
    is_delete  int default 0 not null
);

create index userid
    on term_score (userid);

create table ums_follow
(
    id          bigint auto_increment
        primary key,
    self_id     bigint null comment '用户的id',
    follow_id   bigint null comment '关注人的id 可以是身份id 也可以是用户id',
    create_time datetime null comment '创建时间',
    create_by   varchar(32) null comment '创建者',
    update_time datetime null comment '更新时间',
    update_by   varchar(32) null comment '更新者',
    status      tinyint(1) default 0 null comment '0:关注，1：取消',
    type        tinyint(1) null comment '0:用户 1：社区身份'
) collate = utf8_unicode_ci;

create table ums_identity
(
    id             bigint auto_increment comment 'id'
        primary key,
    uid            bigint                             not null comment '用户id',
    name           varchar(255)                       not null comment '名称',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    is_delete      tinyint  default 0                 not null comment '0未删除、1已删除',
    comment_count  bigint   default 0 null comment '评论数',
    followed_count bigint   default 0 null comment '被关注数',
    post_count     bigint   default 0 null comment '发帖数',
    like_count     bigint   default 0 null comment '获赞数',
    avatar         varchar(1000) null comment '头像',
    type           tinyint(1) default 1 null comment '0:实名 1：匿名',
    change_time    datetime null comment '用于记录修改昵称的时间'
) comment '用户身份表' collate = utf8_bin;

create
fulltext index ums_identity_ft_index
    on ums_identity (name);

create table ums_menu
(
    id          bigint auto_increment
        primary key,
    menu_name   varchar(64) collate utf8mb4_unicode_ci null comment '接口名称',
    perms       varchar(100) collate utf8mb4_unicode_ci null comment '接口标识',
    create_by   varchar(32) charset utf8mb4 null comment '创建者',
    create_time datetime null comment '创建时间',
    update_by   varchar(32) charset utf8mb4 null comment '更新者',
    update_time datetime null comment '更新时间',
    is_delete   tinyint(1) default 0 null comment '删除标志（0代表未删除，1代表已删除）',
    constraint ums_menu_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table ums_page_view
(
    id          bigint auto_increment comment '主键'
        primary key,
    view_id     bigint null comment '浏览的的文章的id',
    user_id     bigint null comment '用户id',
    create_time datetime null comment '创建时间',
    create_by   varchar(32) null comment '创建者',
    update_time datetime null comment '更新时间',
    update_by   varchar(32) null comment '更新者',
    is_delete   tinyint(1) null comment '逻辑删除',
    constraint ums_page_view_id_uindex
        unique (id)
) comment '我的浏览' collate = utf8_unicode_ci;

create table ums_role
(
    id          bigint auto_increment
        primary key,
    name        varchar(128) collate utf8mb4_unicode_ci null,
    role_key    varchar(100) collate utf8mb4_unicode_ci null comment '角色标识字符串',
    create_by   varchar(32) charset utf8mb4 null comment '创建者',
    create_time datetime null comment '创建时间',
    update_by   varchar(32) charset utf8mb4 null comment '更新者',
    update_time datetime null comment '更新时间',
    is_delete   tinyint(1) default 0 null comment '删除标志（0代表未删除，1代表已删除）',
    constraint ums_role_id_uindex
        unique (id)
) collate = utf8_unicode_ci;

create table ums_role_menu
(
    id      bigint auto_increment
        primary key,
    role_id bigint not null comment '角色ID',
    menu_id bigint not null comment '菜单ID'
) collate = utf8_unicode_ci;

create table ums_user
(
    id                     bigint auto_increment comment '主键'
        primary key,
    unionid                varchar(90) null comment '用户在开放平台的唯一标识符',
    user_name              varchar(64) collate utf8mb4_unicode_ci null comment '用户名',
    password               varchar(128) collate utf8mb4_unicode_ci null comment '密码',
    create_by              varchar(32) charset utf8mb4 null comment '创建者',
    create_time            datetime null comment '创建时间',
    update_by              varchar(32) charset utf8mb4 null comment '更新者',
    update_time            datetime null comment '更新时间',
    is_delete              tinyint(1) default 0 null comment '删除标志（0代表未删除，1代表已删除）',
    version                varchar(100) default '0' null comment '数据更新版本',
    avatar                 varchar(1000) null comment '用户头像',
    school_no              varchar(32) null comment '学号',
    email                  varchar(255) null comment '邮箱地址',
    openid                 varchar(90) null comment '微信openid',
    service_account_openid varchar(90) null,
    constraint ums_user_id_uindex
        unique (id),
    constraint ums_user_unionid_uindex
        unique (unionid)
) collate = utf8_unicode_ci;

create
fulltext index ums_user_ft_index
    on ums_user (user_name);

create table ums_user_conf
(
    id          bigint auto_increment
        primary key,
    uid         bigint null comment '用户id',
    conf_key    varchar(128) null comment '设置id',
    conf_value  varchar(1024) null comment '设置value',
    update_time datetime null comment '更新时间',
    constraint ums_user_conf_id_uindex
        unique (id)
);

create table ums_user_role
(
    id      bigint auto_increment comment '主键'
        primary key,
    user_id bigint null comment '用户id',
    role_id bigint null comment '角色id'
) collate = utf8_unicode_ci;

create table user_energy
(
    id           bigint auto_increment
        primary key,
    userid       bigint      not null,
    type         int         not null,
    energy_count double      not null,
    energy_sum   double      not null,
    create_time  datetime    not null,
    description  varchar(30) not null,
    relate_id    bigint null
);

