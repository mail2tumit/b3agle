-- liquibase formatted sql

-- changeset tumit:02
ALTER TABLE users
    ADD COLUMN email VARCHAR(255);

-- rollback ALTER TABLE users DROP COLUMN email;

-- tagDatabase V2
