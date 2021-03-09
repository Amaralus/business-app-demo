create table if not exists sandbox.business_app.alpha_version
(
    version_id    varchar(128) not null,
    version_value varchar(32)  not null,
    alpha_code    varchar(128) not null,
    update_field  varchar(256),
    modified_date timestamp    not null default now(),
    row_version   bigint       not null default 1,
    deleted       varchar(1)   not null default 'N',

    constraint pk_alpha_version_version_id primary key (version_id),
    constraint fk_alphaversion_alphacode_alpha_alphacode foreign key (alpha_code) references sandbox.business_app.alpha (alpha_code),
    constraint uc_alphaverison_versionvalue_alphacode unique (version_value, alpha_code)
);

create index if not exists idx_alphaversion_versionvalue on sandbox.business_app.alpha_version (version_value);
create index if not exists idx_alphaversion_alphacode on sandbox.business_app.alpha_version (alpha_code);