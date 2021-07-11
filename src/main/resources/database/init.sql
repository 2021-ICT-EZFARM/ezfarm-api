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
    time_zone = 'Asia/Seoul';

CREATE TABLE test
(
    id      bigint(20) NOT NULL AUTO_INCREMENT,
    content varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
INSERT INTO test(content) value ('테스트');
SELECT *
FROM test;