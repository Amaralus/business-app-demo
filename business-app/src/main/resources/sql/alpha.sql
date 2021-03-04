create table if not exists sandbox.business_app.alpha
(
    alpha_code    varchar(128) not null,
    modified_date timestamp    not null default now(),
    row_version   bigint       not null default 1,
    deleted       varchar(1)   not null default 'N',

    constraint pk_alpha_alpha_code primary key (alpha_code)
);