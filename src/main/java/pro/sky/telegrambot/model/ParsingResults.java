package pro.sky.telegrambot.model;

import java.time.LocalDateTime;

public class ParsingResults {

    private LocalDateTime dateTime;
    private String reminderDescription;
    private String status;

    public ParsingResults() {
    }

    public ParsingResults(LocalDateTime dateTime, String reminderDescription, String status) {
        this.dateTime = dateTime;
        this.reminderDescription = reminderDescription;
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
