--liquibase formatted sql

--changeset Nikolay:1
CREATE TABLE notification_task
(
    id       INT,
    chatId   INT,
    message  CHARACTER,
    timeDate CHARACTER
);

--changeset sbukaevsky:10
CREATE TABLE cats
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
