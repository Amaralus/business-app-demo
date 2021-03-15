create table if not exists sandbox.business_app.theta
(
    theta_id      varchar(36)  not null,
    theta_code    varchar(128) not null,
    modified_date timestamp    not null default now(),
    row_version   bigint       not null default 1,
    deleted       varchar(1)   not null default 'N',

    constraint pk_theta_theta_id primary key (theta_id),
    constraint uc_theta_theta_code unique (theta_code)
);

create index if not exists fk_theta_theta_code on sandbox.business_app.theta (theta_code);
