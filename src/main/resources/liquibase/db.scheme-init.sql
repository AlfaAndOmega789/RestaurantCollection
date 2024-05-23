create sequence if not exists res_id_seq;
------------------------------------------------------------------------------------------------------------------------
create table if not exists restaurants
(
    id bigint default nextval('res_id_seq'::regclass) not null
        constraint restaurant_pkey
            primary key,
    name varchar(100) not null,
    city varchar(30) not null,
    estimated_cost int,
    average_rating numeric,
    votes int
);

create index if not exists idx_fk_restaurants_city on restaurants (city);