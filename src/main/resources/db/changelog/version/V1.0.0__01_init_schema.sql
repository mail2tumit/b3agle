-- liquibase formatted sql

-- changeset b3agle:V1.0.0__01
CREATE TABLE stock
(
    abbr VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255)
);

-- rollback DROP TABLE stock;
