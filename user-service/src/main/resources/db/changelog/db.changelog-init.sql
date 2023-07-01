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

DROP TABLE IF EXISTS refresh_tokens;
DROP SEQUENCE IF EXISTS refresh_tokens_seq;

CREATE SEQUENCE refresh_tokens_seq start 1 increment 1;

CREATE TABLE refresh_tokens
(
    id              BIGINT DEFAULT nextval('refresh_tokens_seq') PRIMARY KEY,
    token           VARCHAR(255) NOT NULL UNIQUE,
    expiration_time TIMESTAMP,
    user_id         BIGINT REFERENCES users(id) ON DELETE CASCADE
);

