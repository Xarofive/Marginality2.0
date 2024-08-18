DROP TABLE IF EXISTS CHICKEN;

CREATE TABLE CHICKEN
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    cost        INT,
    is_for_sale BOOLEAN,
    count       INT,
    date        DATE
);

DROP TABLE IF EXISTS EGG;

CREATE TABLE EGG
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    cost        INT,
    is_for_sale BOOLEAN,
    count       INT,
    date        DATE
);

DROP TABLE IF EXISTS MEAT;

CREATE TABLE MEAT
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    cost        INT,
    is_for_sale BOOLEAN,
    count       INT,
    date        DATE
);