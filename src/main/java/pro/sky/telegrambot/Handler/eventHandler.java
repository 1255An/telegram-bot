package pro.sky.telegrambot.Handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.StateCash;

import java.util.Date;

public class eventHandler {

    private final StateCash stateCash;
    private NotificationTask notificationTask = new NotificationTask();

    public eventHandler(StateCash stateCash) {
        this.stateCash = stateCash;
    }

    public static NotificationTask enterNotification (Message message,long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, message.text());
        Date date;
        try {
            date = parseDate
        }

    }
}
