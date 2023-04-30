create table products
(
    id          bigint               not null auto_increment,
    name        varchar(200)         not null,
    description text                 not null,
    category    varchar(20)          not null,
    available   boolean default true not null,
    primary key (id)
);

create table product_variations
(
    id          bigint        not null auto_increment,
    size_name   varchar(200)  not null,
    description text          not null,
    available   boolean       not null,
    price       decimal(6, 2) not null,
    product_id  bigint        not null,
    primary key (id),
    constraint fk_product_variations_product_id FOREIGN KEY (product_id) REFERENCES products (id)
);
