# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  picture_url               varchar(255),
  notes                     varchar(255),
  created_on                datetime,
  portlets_count            integer,
  portlets_counted_on       datetime,
  constraint pk_category primary key (id))
;

create table portlet (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  owner_id                  bigint,
  picture_url               varchar(255),
  notes                     varchar(255),
  validity                  integer,
  visible_to_all            tinyint(1) default 0,
  primary_exchange          varchar(255),
  last_rebalanced_on        datetime,
  created_on                datetime,
  follower_count            bigint,
  volatility                double,
  total_return              double,
  daily_return              double,
  annual_return             double,
  constraint ck_portlet_validity check (validity in (0,1,2,3)),
  constraint pk_portlet primary key (id))
;

create table portlet_stock (
  id                        bigint auto_increment not null,
  portlet_id                bigint,
  stock                     varchar(255),
  percent                   double,
  last_updated_on           datetime,
  constraint pk_portlet_stock primary key (id))
;

create table sector (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  picture_url               varchar(255),
  notes                     varchar(255),
  created_on                datetime,
  portlet_count             integer,
  portlets_counted_on       datetime,
  constraint pk_sector primary key (id))
;

create table stock (
  id                        bigint auto_increment not null,
  symbol                    varchar(255),
  name                      varchar(255),
  primary_exchange          varchar(255),
  type                      varchar(255),
  constraint pk_stock primary key (id))
;

create table stock_stats (
  id                        bigint auto_increment not null,
  exchange                  varchar(255),
  stock_id                  bigint,
  date                      varchar(255),
  local_date                date,
  open_price                varchar(255),
  close_price               varchar(255),
  high_price                varchar(255),
  low_price                 varchar(255),
  volume                    varchar(255),
  avg_vol                   varchar(255),
  mktcap                    varchar(255),
  activity                  varchar(255),
  constraint pk_stock_stats primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
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
  male                      tinyint(1) default 0,
  email_verified            tinyint(1) default 0,
  validity                  integer,
  timezone                  integer,
  birthday                  varchar(255),
  follower_count            integer,
  following_count           integer,
  portlet_created_count     integer,
  constraint ck_user_validity check (validity in (0,1,2,3)),
  constraint pk_user primary key (id))
;

create table user_portlet_stock (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  portlet_id                bigint,
  stock                     varchar(255),
  qty                       double,
  buy_price                 double,
  buy_weight                double,
  buy_epoch                 bigint,
  constraint pk_user_portlet_stock primary key (id))
;


create table portlet_sector (
  portlet_id                     bigint not null,
  sector_id                      bigint not null,
  constraint pk_portlet_sector primary key (portlet_id, sector_id))
;
alter table portlet add constraint fk_portlet_owner_1 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_portlet_owner_1 on portlet (owner_id);
alter table portlet_stock add constraint fk_portlet_stock_portlet_2 foreign key (portlet_id) references portlet (id) on delete restrict on update restrict;
create index ix_portlet_stock_portlet_2 on portlet_stock (portlet_id);
alter table stock_stats add constraint fk_stock_stats_stock_3 foreign key (stock_id) references stock (id) on delete restrict on update restrict;
create index ix_stock_stats_stock_3 on stock_stats (stock_id);
alter table user_portlet_stock add constraint fk_user_portlet_stock_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_portlet_stock_user_4 on user_portlet_stock (user_id);
alter table user_portlet_stock add constraint fk_user_portlet_stock_portlet_5 foreign key (portlet_id) references portlet (id) on delete restrict on update restrict;
create index ix_user_portlet_stock_portlet_5 on user_portlet_stock (portlet_id);



alter table portlet_sector add constraint fk_portlet_sector_portlet_01 foreign key (portlet_id) references portlet (id) on delete restrict on update restrict;

alter table portlet_sector add constraint fk_portlet_sector_sector_02 foreign key (sector_id) references sector (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;

drop table portlet;

drop table portlet_sector;

drop table portlet_stock;

drop table sector;

drop table stock;

drop table stock_stats;

drop table user;

drop table user_portlet_stock;

SET FOREIGN_KEY_CHECKS=1;

