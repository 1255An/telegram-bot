package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String ResponseMessage;
    private LocalDateTime DateTime;

    public NotificationTask() {
    }

    public NotificationTask(Long chatId, String responseMessage, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.ResponseMessage = responseMessage;
        this.DateTime = dateTime;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;
        NotificationTask that = (NotificationTask) o;
        return chatId == that.chatId && Objects.equals(ResponseMessage, that.ResponseMessage) && Objects.equals(DateTime, that.DateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, ResponseMessage, DateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "chatId=" + chatId +
                ", ResponseMessage='" + ResponseMessage + '\'' +
                ", DateTime=" + DateTime +
                '}';
    }
}
