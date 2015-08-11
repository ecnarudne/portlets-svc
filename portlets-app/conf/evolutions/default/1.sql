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
  volatility_class          integer,
  primary_exchange          varchar(255),
  last_rebalanced_on        datetime,
  created_on                datetime,
  constraint ck_portlet_validity check (validity in (0,1,2,3)),
  constraint ck_portlet_volatility_class check (volatility_class in (0,1,2)),
  constraint pk_portlet primary key (id))
;

create table portlet_definition_audit (
  id                        bigint auto_increment not null,
  portlet_id                bigint,
  stock                     varchar(255),
  epoch                     bigint,
  weightage                 double,
  last_updated_on           datetime,
  constraint pk_portlet_definition_audit primary key (id))
;

create table portlet_investment_audit (
  id                        bigint auto_increment not null,
  portlet_id                bigint,
  user_id                   bigint,
  stock                     varchar(255),
  epoch                     bigint,
  qty                       double,
  price                     double,
  weight                    double,
  constraint pk_portlet_investment_audit primary key (id))
;

create table portlet_stock (
  id                        bigint auto_increment not null,
  portlet_id                bigint,
  stock                     varchar(255),
  weightage                 double,
  last_updated_on           datetime,
  constraint pk_portlet_stock primary key (id))
;

create table price_import_history (
  id                        bigint auto_increment not null,
  exchange                  varchar(255),
  local_date                date,
  import_date               datetime,
  filepath                  varchar(255),
  constraint pk_price_import_history primary key (id))
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
  local_date                date,
  open_price                double,
  close_price               double,
  high_price                double,
  low_price                 double,
  avg_vol                   double,
  mktcap                    double,
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

create table user_token (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  token                     varchar(255),
  first_signin_time         datetime,
  provider                  varchar(255),
  federated_user_id         varchar(255),
  federatedcreation_time    datetime,
  application_expiry_time   datetime,
  federated_expiry_seconds  integer,
  constraint pk_user_token primary key (id))
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
alter table user_token add constraint fk_user_token_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_token_user_6 on user_token (user_id);



alter table portlet_sector add constraint fk_portlet_sector_portlet_01 foreign key (portlet_id) references portlet (id) on delete restrict on update restrict;

alter table portlet_sector add constraint fk_portlet_sector_sector_02 foreign key (sector_id) references sector (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;

drop table portlet;

drop table portlet_sector;

drop table portlet_definition_audit;

drop table portlet_investment_audit;

drop table portlet_stock;

drop table price_import_history;

drop table sector;

drop table stock;

drop table stock_stats;

drop table user;

drop table user_portlet_stock;

drop table user_token;

SET FOREIGN_KEY_CHECKS=1;

