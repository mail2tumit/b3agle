-- liquibase formatted sql

-- changeset b3agle:V1.1.0__01
ALTER TABLE stock
    ADD COLUMN description VARCHAR(255);

-- rollback ALTER TABLE stock DROP COLUMN description;
