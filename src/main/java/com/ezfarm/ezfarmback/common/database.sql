--database test sql--
use
ezfarm;

show
variables like 'c%';

ALTER
DATABASE ezfarm
CHARACTER SET = 'utf8mb4'
COLLATE = 'utf8mb4_general_ci';

select @@time_zone, now();
set
time_zone='Asia/Seoul';

CREATE TABLE test
(
    id      bigint(20) NOT NULL AUTO_INCREMENT,
    content varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
)ENGINE=InnoDB;
INSERT INTO test(content) value ('테스트');
SELECT *
FROM test;

--table sql--
create table user
(
    user_id      bigint(20) NOT NULL AUTO_INCREMENT,
    name         varchar(20)  NOT NULL,
    email        varchar(50)  NOT NULL,
    password     varchar(255) NOT NULL,
    role         varchar(20)  NOT NULL,
    address      varchar(100) DEFAULT NULL,
    image_url    varchar(255) DEFAULT NULL,
    phone_number varchar(255) DEFAULT NULL,
    created_date timestamp,
    updated_date timestamp,
    primary key (user_id)
)

create table farm
(
    farm_id      bigint(20) NOT NULL AUTO_INCREMENT,
    user_id      bigint(20) NOT NULL,
    name         varchar(50) NOT NULL,
    is_main      boolean     NOT NULL,
    phone_number varchar(50)  DEFAULT NULL,
    address      varchar(100) DEFAULT NULL,
    area         varchar(50)  DEFAULT NULL,
    crop_type    enum('TOMATO', 'STRAWBERRY', 'PAPRIKA') DEFAULT NULL,
    farm_type    enum('VINYL', 'GLASS') DEFAULT NULL,
    start_date   date,
    created_date timestamp,
    updated_date timestamp,
    primary key (farm_id)
)

create table favorite
(
    favorite_id bigint(20) NOT NULL AUTO_INCREMENT,
    farm_id     bigint(20) NOT NULL,
    user_id     bigint(20) NOT NULL,
    primary key (favorite_id)
)

create table remote
(
    remote_id    bigint(20) NOT NULL AUTO_INCREMENT,
    farm_id      bigint(20) NOT NULL,
    co2          enum('ON','OFF') NOT NULL,
    illuminance  enum('ON','OFF') NOT NULL,
    temperature  float NOT NULL,
    water        enum('ON','OFF') NOT NULL,
    created_date timestamp,
    updated_date timestamp,
    primary key (remote_id)
)

create table alert_range
(
    alert_range_id bigint(20) NOT NULL AUTO_INCREMENT,
    farm_id        bigint(20) NOT NULL,
    co2_max        float NOT NULL,
    co2_min        float NOT NULL,
    humidity_max   float NOT NULL,
    humidity_min   float NOT NULL,
    imn_max        float NOT NULL,
    imn_min        float NOT NULL,
    mos_max        float NOT NULL,
    mos_min        float NOT NULL,
    ph_max         float NOT NULL,
    ph_min         float NOT NULL,
    tmp_max        float NOT NULL,
    tmp_min        float NOT NULL,
    created_date   timestamp,
    updated_date   timestamp,
    primary key (alert_range_id)
)

--fk sql--

alter table alert_range
    (
    add constraint 'alert_range_farm_id_fkey'
    foreign key (farm_id)
    references farm
    )

alter table farm(
    add constraint 'farm_user_id_fkey'
    foreign key (user_id)
    references user
    )

alter table favorite
    (
    add constraint 'favorite_farm_id_fkey'
    foreign key (farm_id)
    references farm
    )

alter table favorite
    (
    add constraint 'alert_user_id_fkey'
    foreign key (user_id)
    references user
    )

alter table remote
    (
    add constraint 'remote_farm_id_fkey'
    foreign key (farm_id)
    references farm
    )