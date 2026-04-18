-- liquibase formatted sql

-- changeset tumit:V1.0.0
CREATE TABLE users
(
    id       BIGINT PRIMARY KEY,
    username VARCHAR(100) NOT NULL
);

-- rollback DROP TABLE users;

-- tagDatabase V1