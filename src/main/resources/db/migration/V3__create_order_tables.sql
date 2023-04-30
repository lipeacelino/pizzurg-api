create table orders
(
    id             bigint        not null auto_increment,
    user_id        bigint        not null,
    status         varchar(30)   not null,
    payment_method varchar(20)   not null,
    amount         decimal(6, 2) not null,
    created_date   datetime      not null,
    primary key (id),
    constraint fk_orders_user_id foreign key (user_id) references users (id)
);

create table order_items
(
    id                   bigint not null auto_increment,
    product_variation_id bigint not null,
    order_id             bigint not null,
    quantity             int,
    primary key (id),
    constraint fk_order_items_product_variation_id foreign key (product_variation_id) references product_variations (id),
    constraint fk_order_items_order_id foreign key (order_id) references orders (id)
);

create table deliveries_data
(
    id            bigint      not null auto_increment,
    order_id      bigint      not null,
    receiver_name text        not null,
    address       text        not null,
    house_number  char(10)    not null,
    complement    text        not null,
    district      varchar(30) not null,
    zip_code      varchar(9)  not null,
    city          text        not null,
    state         char(2)     not null,
    phone_number  char(15)    not null,
    primary key (id),
    constraint fk_deliveries_data_order_id foreign key (order_id) references orders (id)
);