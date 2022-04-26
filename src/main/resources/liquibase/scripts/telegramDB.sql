-- liquibase formatted sql

-- changeset atrf:1
CREATE TABLE NotificationTasks (
ChatId SERIAL PRIMARY KEY,
ResponseMessage TEXT,
DateTime TIMESTAMP WITH TIME ZONE
);
