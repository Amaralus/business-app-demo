create table if not exists sandbox.business_app.role
(
    name varchar(64) not null,

    constraint pk_role_name primary key (name)
);