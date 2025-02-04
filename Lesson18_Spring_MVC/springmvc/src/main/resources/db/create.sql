create table public.menu_items(
    id bigint generated by default as identity primary key,
    create_date timestamp(6),
    description varchar(255)     not null,
    name        varchar(255)     not null,
    price       double precision not null,
    update_date timestamp(6)
);

-- alter table menu_items
--     owner to postgres;