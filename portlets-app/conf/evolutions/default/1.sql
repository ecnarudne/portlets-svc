# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table portlet (
  id                        bigint not null,
  name                      varchar(255),
  owner_id                  bigint,
  picture_url               varchar(255),
  notes                     varchar(255),
  validity                  integer,
  visible_to_all            boolean,
  created_on                timestamp,
  constraint ck_portlet_validity check (validity in (0,1,2,3)),
  constraint pk_portlet primary key (id))
;

create table portlet_stock (
  id                        bigint not null,
  portlet_id                bigint,
  stock                     varchar(255),
  percent                   double,
  last_updated_on           timestamp,
  constraint pk_portlet_stock primary key (id))
;

create table stock (
  id                        bigint not null,
  symbol                    varchar(255),
  name                      varchar(255),
  primary_exchange          varchar(255),
  type                      varchar(255),
  constraint pk_stock primary key (id))
;

create table user (
  id                        bigint not null,
  full_name                 varchar(255),
  email                     varchar(255),
  preferred_name            varchar(255),
  name_title                varchar(255),
  profile_title             varchar(255),
  profile_description       varchar(255),
  google_id                 varchar(255),
  facebook_id               varchar(255),
  profile_link              varchar(255),
  profile_picture           varchar(255),
  locale                    varchar(255),
  male                      boolean,
  email_verified            boolean,
  validity                  integer,
  timezone                  integer,
  birthday                  varchar(255),
  constraint ck_user_validity check (validity in (0,1,2,3)),
  constraint pk_user primary key (id))
;

create table user_portlet_stock (
  id                        bigint not null,
  user_id                   bigint,
  portlet_id                bigint,
  stock                     varchar(255),
  qty                       double,
  buy_price                 double,
  buy_epoch                 bigint,
  constraint pk_user_portlet_stock primary key (id))
;

create sequence portlet_seq;

create sequence portlet_stock_seq;

create sequence stock_seq;

create sequence user_seq;

create sequence user_portlet_stock_seq;

alter table portlet add constraint fk_portlet_owner_1 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_portlet_owner_1 on portlet (owner_id);
alter table portlet_stock add constraint fk_portlet_stock_portlet_2 foreign key (portlet_id) references portlet (id) on delete restrict on update restrict;
create index ix_portlet_stock_portlet_2 on portlet_stock (portlet_id);
alter table user_portlet_stock add constraint fk_user_portlet_stock_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_portlet_stock_user_3 on user_portlet_stock (user_id);
alter table user_portlet_stock add constraint fk_user_portlet_stock_portlet_4 foreign key (portlet_id) references portlet (id) on delete restrict on update restrict;
create index ix_user_portlet_stock_portlet_4 on user_portlet_stock (portlet_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists portlet;

drop table if exists portlet_stock;

drop table if exists stock;

drop table if exists user;

drop table if exists user_portlet_stock;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists portlet_seq;

drop sequence if exists portlet_stock_seq;

drop sequence if exists stock_seq;

drop sequence if exists user_seq;

drop sequence if exists user_portlet_stock_seq;

