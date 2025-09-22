
create table if not exists events
(
    id         bigint auto_increment
        primary key,
    event_date datetime(6)  null,
    title      varchar(255) null
);

create table if not exists users
(
    id       bigint auto_increment
        primary key,
    password varchar(255) not null,
    username varchar(255) not null,
    constraint idx_users_username
        unique (username)
);

create table if not exists events_users
(
    event_entity_id bigint not null,
    users_id        bigint not null,
    primary key (event_entity_id, users_id),
    constraint FK2xqmgftqei5c4s82ruxl74w9a
        foreign key (users_id) references users (id),
    constraint FKisytkthhhl5p2pcdsa1rtjj6f
        foreign key (event_entity_id) references events (id)
);

create table if not exists owe_summary
(
    id          bigint auto_increment
        primary key,
    amount      decimal(38, 2) null,
    creditor_id bigint         null,
    debtor_id   bigint         null,
    constraint UKq8elwfgbgqaq4dkme065n3646
        unique (debtor_id, creditor_id),
    constraint FK8v0ppr92t4lx83kyt978gbcoo
        foreign key (creditor_id) references users (id),
    constraint FKfgliwk5qyo42a672h0tgo1kpk
        foreign key (debtor_id) references users (id)
);

create table if not exists transactions
(
    id             bigint auto_increment
        primary key,
    amount         decimal(38, 2)              null,
    description    varchar(255)                null,
    split_strategy enum ('EQUALS', 'UNEQUALS') null,
    event_id       bigint                      null,
    owner_id       bigint                      null,
    constraint FKe01y6qhe8lfkj74f0nac6mbmk
        foreign key (event_id) references events (id),
    constraint FKs9nianm3lvoipmhypfiy84tiw
        foreign key (owner_id) references users (id)
);

create table if not exists owes
(
    id             bigint auto_increment
        primary key,
    amount         decimal(18, 2)           not null,
    status         enum ('PAID', 'PENDING') not null,
    creditor_id    bigint                   not null,
    debtor_id      bigint                   not null,
    transaction_id bigint                   not null,
    constraint FK1luvfxegpx8xcj9omhfmwjhfe
        foreign key (transaction_id) references transactions (id),
    constraint FK64ds7swvuuint64mguhss651h
        foreign key (debtor_id) references users (id),
    constraint FKdply0aefrnogijcescg4ai5nu
        foreign key (creditor_id) references users (id)
);

create table if not exists users_events
(
    user_entity_id bigint not null,
    events_id      bigint not null,
    primary key (user_entity_id, events_id),
    constraint FK56qu3rya56tll071qq9emrfwn
        foreign key (events_id) references events (id),
    constraint FKbntp93bq4cwr5gc644nv3y1yn
        foreign key (user_entity_id) references users (id)
);





insert into users (id, password, username) VALUES (1,'','u1');
insert into users (id, password, username) VALUES (2,'','u2');
insert into users (id, password, username) VALUES (3,'','u3');
insert into users (id, password, username) VALUES (4,'','u4');
insert into users (id, password, username) VALUES (5,'','u5');

insert into events (id,event_date, title) values (1,NOW(),'e1');
insert into events (id,event_date, title) values (2,NOW(),'e2');
insert into events (id,event_date, title) values (3,NOW(),'e3');

insert into events_users(event_entity_id, users_id) values (1,1);
insert into events_users(event_entity_id, users_id) values (1,2);
insert into events_users(event_entity_id, users_id) values (1,3);