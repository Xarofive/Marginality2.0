DROP TABLE IF EXISTS MEAL;

CREATE TABLE MEAL
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(255),
    cost   INT,
    status VARCHAR(50),
    count  INT,
    date   DATE,
    CONSTRAINT status_check CHECK (status IN ('FOR_SALE', 'SOLD', 'EXPIRED', 'STORAGE', 'DAMAGED'))
);

INSERT INTO MEAL (name, cost, status, count, date)
VALUES ('Meal 1', 100, 'FOR_SALE', 10, '2024-08-01'),
       ('Meal 2', 150, 'SOLD', 5, '2024-08-02'),
       ('Meal 3', 200, 'EXPIRED', 20, '2024-08-03'),
       ('Meal 4', 80, 'STORAGE', 15, '2024-08-04'),
       ('Meal 5', 120, 'DAMAGED', 8, '2024-08-05');