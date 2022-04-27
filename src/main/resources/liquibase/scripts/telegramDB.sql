-- liquibase formatted sql

-- changeset atrf:4
CREATE TABLE NotificationTask (
id SERIAL PRIMARY KEY,
chatId REAL,
notificationDescription TEXT,
dateTime TIMESTAMP WITH TIME ZONE
);




