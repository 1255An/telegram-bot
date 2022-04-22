-- liquibase formatted sql

-- changeset aTrf:1
CREATE TABLE Tasks (
ChatId SERIAL PRIMARY KEY,
ResponseMessage TEXT,
DateTime TIMESTAMP WITH TIME ZONE
);
