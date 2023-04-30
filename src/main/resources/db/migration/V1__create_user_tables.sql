create table users
(
    id       bigint       not null auto_increment,
    email    varchar(100) not null,
    password varchar(60)  not null,
    primary key (id)
);

create table roles
(
    id        bigint      not null auto_increment,
    name varchar(30) not null,
    primary key (id)
);

create table users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_users_roles_user_id foreign key (user_id) REFERENCES users (id),
    constraint fk_users_roles_role_id foreign key (role_id) REFERENCES roles (id)
);
