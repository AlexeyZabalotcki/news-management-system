-- liquibase formatted sql

-- changeset create tables:add constraints

DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS users_seq;

CREATE SEQUENCE users_seq start 3 increment 1;

CREATE TABLE users
(
    id          BIGINT DEFAULT nextval('users_seq') PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR      NOT NULL,
    role        VARCHAR (30) NOT NULL
);
