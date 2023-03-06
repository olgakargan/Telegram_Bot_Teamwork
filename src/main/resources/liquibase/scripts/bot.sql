--liquibase formatted sql

--changeset olgakargan:1
CREATE TABLE notification_task
(
    id       INT,
    chatId   INT,
    message  CHARACTER,
    timeDate CHARACTER
);

<<<<<<< HEAD
--changeset sbukaevsky:2
CREATE TABLE users
(
    id        BIGINT,
    user_name TEXT,
    user_id   BIGINT,
    phone     TEXT
);


--changeset sbukaevsky:8
DROP TABLE users;

--changeset sbukaevsky:9
ALTER TABLE users ALTER COLUMN id TYPE BIGINT;


--changeset olgakargan:2
create table reportData

(
    id          integer PRIMARY KEY not null,
    chat_id     integer             not null,
    ration      varchar             not null,
    health      varchar             not null,
    habits      varchar             not null,
    filePath    varchar             not null,
    days        integer             not null,
    person_id   integer             not null,
    caption     varchar             not null,
    lastMessage date                not null,
    data        bytea               not null
);
--changeset olgakargan:3
CREATE TABLE pet
(
    id              BIGINT,
    breed           TEXT,
    pet_name        TEXT,
    year_of_birth   INT,
    description     TEXT
)
=======
>>>>>>> origin/dev
