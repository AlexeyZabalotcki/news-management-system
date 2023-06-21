-- liquibase formatted sql

-- changeset create tables:add constraints

DROP TABLE IF EXISTS news;
DROP SEQUENCE IF EXISTS news_seq;

CREATE SEQUENCE news_seq start 1 increment 1;

CREATE TABLE news
(
    id          BIGINT DEFAULT nextval('news_seq') PRIMARY KEY,
    title       VARCHAR(300) NOT NULL ,
    username    VARCHAR(300) NOT NULL ,
    text        VARCHAR(150) NOT NULL,
    time        TIMESTAMP
);

DROP TABLE IF EXISTS comments;
CREATE SEQUENCE comments_seq start 1 increment 1;

CREATE TABLE comments
(
    id          BIGINT DEFAULT nextval('comments_seq') PRIMARY KEY,
    time        TIMESTAMP,
    text        VARCHAR NOT NULL,
    username    VARCHAR(300) NOT NULL,
    news_id     BIGINT NOT NULL,
    CONSTRAINT fk_comments_news_id FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE
);