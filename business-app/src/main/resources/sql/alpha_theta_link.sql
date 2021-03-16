create table if not exists sandbox.business_app.alpha_theta_link
(
    link_id       varchar(128) not null,
    alpha_code    varchar(128) not null,
    theta_id      varchar(36)  not null,
    modified_date timestamp    not null default now(),
    row_version   bigint       not null default 1,
    deleted       varchar(1)   not null default 'N',

    constraint pk_alphathetalink_linkid primary key (link_id),
    constraint uc_alphathetalink_alphacode_thetaid unique (alpha_code, theta_id),
    constraint fk_alphathetalink_alphacode_alpha_alphacode foreign key (alpha_code) references sandbox.business_app.alpha (alpha_code),
    constraint fk_alphathetalink_thetaid_theta_thetaid foreign key (theta_id) references sandbox.business_app.theta (theta_id)
);