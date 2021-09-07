create table if not exists sandbox.business_app.user_t
(
    username varchar(64) not null,
    password varchar(256) not null,

    constraint pk_user_username primary key (username)
);