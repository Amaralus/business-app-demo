create table if not exists sandbox.business_app.user_role
(
    username varchar(64) not null,
    role_name varchar(64) not null,

    constraint pk_userrole_username_name primary key (username, role_name),
    constraint fk_userrole_username_user_username foreign key (username) references sandbox.business_app.user_t(username),
    constraint fk_userrole_rolename_role_name foreign key (role_name) references sandbox.business_app.role(name)
)