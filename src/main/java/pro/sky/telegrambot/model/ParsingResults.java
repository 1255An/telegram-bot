package pro.sky.telegrambot.model;

import java.time.LocalDateTime;

public class ParsingResults {

    private LocalDateTime dateTime;
    private String notificationDescription;
    private String status;

    public ParsingResults() {
    }

    public ParsingResults(LocalDateTime dateTime, String notificationDescription, String status) {
        this.dateTime = dateTime;
        this.notificationDescription = notificationDescription;
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
