--liquibase formatted sql

--changeset liquibase-demo-service:add-user-and-role
INSERT INTO users (id, password,  username, role)
VALUES ( 1, '$2a$12$SqqoaO5cLmBSbx/191508OjNTWx/kGwmZaOE0VgPohrIPggiqWmEu', 'admin', 'ADMIN'),
        ( 2, '$2a$12$SqqoaO5cLmBSbx/191508OjNTWx/kGwmZaOE0VgPohrIPggiqWmEu', 'subscriber', 'SUBSCRIBER'),
        ( 3, '$2a$12$SqqoaO5cLmBSbx/191508OjNTWx/kGwmZaOE0VgPohrIPggiqWmEu', 'journalist', 'JOURNALIST');

