-- liquibase formatted sql

-- changeset b3agle:V1.0.0__03
ALTER TABLE stock
    ADD COLUMN description VARCHAR(255);

-- rollback ALTER TABLE stock DROP COLUMN description;

-- tagDatabase V1.0.0
