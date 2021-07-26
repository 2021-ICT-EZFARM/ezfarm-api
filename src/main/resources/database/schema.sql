--table--
create table user
(
    user_id      bigint(20)   NOT NULL AUTO_INCREMENT,
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
);

create table farm
(
    farm_id      bigint(20)  NOT NULL AUTO_INCREMENT,
    user_id      bigint(20)  NOT NULL,
    name         varchar(50) NOT NULL,
    is_main      boolean     NOT NULL,
    farm_group   varchar(20) NOT NULL,
    phone_number varchar(50)                              DEFAULT NULL,
    address      varchar(100)                             DEFAULT NULL,
    area         varchar(50)                              DEFAULT NULL,
    crop_type    enum ('TOMATO', 'STRAWBERRY', 'PAPRIKA') DEFAULT NULL,
    farm_type    enum ('VINYL', 'GLASS')                  DEFAULT NULL,
    start_date   date                                     DEFAULT NULL,
    created_date timestamp,
    updated_date timestamp,
    primary key (farm_id)
);

create table favorite
(
    favorite_id bigint(20) NOT NULL AUTO_INCREMENT,
    farm_id     bigint(20) NOT NULL,
    user_id     bigint(20) NOT NULL,
    primary key (favorite_id)
);

create table remote
(
    remote_id    bigint(20)        NOT NULL AUTO_INCREMENT,
    farm_id      bigint(20)        NOT NULL,
    co2          enum ('ON','OFF') NOT NULL,
    illuminance  enum ('ON','OFF') NOT NULL,
    temperature  float             NOT NULL,
    water        enum ('ON','OFF') NOT NULL,
    created_date timestamp,
    updated_date timestamp,
    primary key (remote_id)
);

create table alert_range
(
    alert_range_id bigint(20) NOT NULL AUTO_INCREMENT,
    farm_id        bigint(20) NOT NULL,
    co2_max        float      NOT NULL,
    co2_min        float      NOT NULL,
    humidity_max   float      NOT NULL,
    humidity_min   float      NOT NULL,
    imn_max        float      NOT NULL,
    imn_min        float      NOT NULL,
    mos_max        float      NOT NULL,
    mos_min        float      NOT NULL,
    ph_max         float      NOT NULL,
    ph_min         float      NOT NULL,
    tmp_max        float      NOT NULL,
    tmp_min        float      NOT NULL,
    created_date   timestamp,
    updated_date   timestamp,
    primary key (alert_range_id)
);

create table facility
(
    facility_id  BIGINT(20) NOT NULL AUTO_INCREMENT,
    farm_id      BIGINT(20) NOT NULL,
    tmp          FLOAT DEFAULT NULL,
    co2          FLOAT DEFAULT NULL,
    humidity     FLOAT DEFAULT NULL,
    illuminance  FLOAT DEFAULT NULL,
    mos          FLOAT DEFAULT NULL,
    ph           FLOAT DEFAULT NULL,
    measure_date TIMESTAMP  NOT NULL,
    primary key (facility_id)
);

create table facility_day_avg
(
    facility_id     BIGINT(20)   NOT NULL AUTO_INCREMENT,
    farm_id         BIGINT(20)   NOT NULL,
    avg_co2         FLOAT DEFAULT NULL,
    avg_humidity    FLOAT DEFAULT NULL,
    avg_illuminance FLOAT DEFAULT NULL,
    avg_mos         FLOAT DEFAULT NULL,
    avg_ph          FLOAT DEFAULT NULL,
    avg_tmp         FLOAT DEFAULT NULL,
    measure_date    varchar(255) NOT NULL,
    primary key (facility_id)
);

create table facility_month_avg
(
    facility_id     BIGINT(20)   NOT NULL AUTO_INCREMENT,
    farm_id         BIGINT(20)   NOT NULL,
    avg_co2         FLOAT DEFAULT NULL,
    avg_humidity    FLOAT DEFAULT NULL,
    avg_illuminance FLOAT DEFAULT NULL,
    avg_mos         FLOAT DEFAULT NULL,
    avg_ph          FLOAT DEFAULT NULL,
    avg_tmp         FLOAT DEFAULT NULL,
    measure_date    varchar(255) NOT NULL,
    primary key (facility_id)
);

create table facility_week_avg
(
    facility_id     BIGINT(20)   NOT NULL AUTO_INCREMENT,
    farm_id         BIGINT(20)   NOT NULL,
    avg_co2         FLOAT DEFAULT NULL,
    avg_humidity    FLOAT DEFAULT NULL,
    avg_illuminance FLOAT DEFAULT NULL,
    avg_mos         FLOAT DEFAULT NULL,
    avg_ph          FLOAT DEFAULT NULL,
    avg_tmp         FLOAT DEFAULT NULL,
    measure_date    varchar(255) NOT NULL,
    primary key (facility_id)
);

create table alert
(
    alert_id     BIGINT(20) NOT NULL AUTO_INCREMENT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    alert_type   INTEGER,
    is_checked   BOOLEAN,
    farm_id      BIGINT(20),
    primary key (alert_id)
);

create table farm_compare_history
(
    fm_cp_hstr_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    farm_id       BIGINT(20),
    user_id       BIGINT(20),
    primary key (fm_cp_hstr_id)
);

create table remote_history
(
    remote_history_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    farm_id           BIGINT(20),
    success_yn        BOOLEAN,
    value             VARCHAR(255),
    created_date      TIMESTAMP,
    updated_date      TIMESTAMP,
    primary key (remote_history_id)
);

--fk--
alter table alert_range
    add constraint alert_range_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);

alter table farm
    add constraint farm_user_id_fkey
        foreign key (user_id)
            references user (user_id);

alter table favorite
    add constraint favorite_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);

alter table favorite
    add constraint alert_user_id_fkey
        foreign key (user_id)
            references user (user_id);

alter table remote
    add constraint remote_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);

alter table facility
    add constraint facility_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);

alter table facility_week_avg
    add constraint facility_week_avg_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);


alter table facility_month_avg
    add constraint facility_month_avg_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);

alter table facility_day_avg
    add constraint facility_day_avg_farm_id_fkey
        foreign key (farm_id)
            references farm (farm_id);