package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.State;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

    private NotificationTask notificationTask = new NotificationTask();

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String message = update.message().text();
            if (!message.isEmpty()) {
                long chatId = update.message().chat().id();
                SendMessage reply;
                State state ;
                switch (message) {
                    case "/start":
                        state = State.NOTIFICATION_MENU;
                        break;
                    case "Create new reminder":
                        state = State.ENTER_NOTIFICATION;
                        break;
                }
                reply = defineReply(state, update);
                try {
                    telegramBot.execute(reply);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public SendMessage defineReply (State state, Update update) {
        SendMessage message =
    }


}
