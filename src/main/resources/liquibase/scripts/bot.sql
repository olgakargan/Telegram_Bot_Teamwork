--liquibase formatted sql

--changeset Nikolay:1
CREATE TABLE notification_task
(
    id       INT,
    chatId   INT,
    message  CHARACTER,
    timeDate CHARACTER
);

<<<<<<< HEAD
--changeset sbukaevsky:10
CREATE TABLE cats
=======
<<<<<<< HEAD
--changeset sbukaevsky:2
CREATE TABLE users
>>>>>>> dev
(
    id            BIGINT,
    cat_name      TEXT,
    breed         TEXT,
    year_of_birth INTEGER,
    description   TEXT
);

--changeset sbukaevsky:11
CREATE TABLE dogs
(
    id            BIGINT,
    dog_name      TEXT,
    breed         TEXT,
    year_of_birth INTEGER,
    description   TEXT
);

--changeset sbukaevsky:14
CREATE TABLE reports_data
(
<<<<<<< HEAD
    id       BIGINT,
    chat_id  BIGINT,
    ration   TEXT,
    health   TEXT,
    habits   TEXT,
    days     INTEGER,
    photo_id BIGINT
<<<<<<< HEAD
);
=======
);
>>>>>>> b0d41d27a7cd3115b27f1ca5bce83357116811cc
=======
    id              BIGINT,
    breed           TEXT,
    pet_name        TEXT,
    year_of_birth   INT,
    description     TEXT
)
=======
>>>>>>> origin/dev
>>>>>>> dev
