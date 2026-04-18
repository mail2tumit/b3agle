-- liquibase formatted sql

-- changeset b3agle:V1.0.0__02
INSERT INTO stock (abbr, name)
VALUES ('BBL', 'Bangkok Bank PCL')
    ON CONFLICT (abbr) DO NOTHING;

INSERT INTO stock (abbr, name)
VALUES ('SCC', 'Siam Cement PCL')
    ON CONFLICT (abbr) DO NOTHING;

-- tagDatabase V1.0.0
