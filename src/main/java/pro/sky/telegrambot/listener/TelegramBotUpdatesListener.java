package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.ParsingResults;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

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
            if (update.message() != null && update.message().text() != null) {
                long chatId = update.message().chat().id();
                Message message = update.message();
                String command = update.message().text();
                if (command.startsWith("/")) {
                    executeCommand(message, command, chatId);
                } else {
                    handleMessage(update, chatId);
                }
                }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleMessage(Update update, long chatId) {
        ParsingResults parsingResults = notificationService.parseMessage(update.message().text());
        if (parsingResults.getStatus().equals("valid")) {
            String responseMessage = parsingResults.getReminderDescription();
            NotificationTask notificationTask = new NotificationTask(chatId, responseMessage,
                    parsingResults.getDateTime());
            notificationService.save(notificationTask);
            logger.info("Saved");
            telegramBot.execute(new SendMessage(chatId, "Sucess!"));
        } else if (parsingResults.getStatus().equals("time is in the past")) {
            telegramBot.execute(new SendMessage(chatId, "Time is in the past"));
        } else {
            telegramBot.execute(new SendMessage(chatId, "Invalid format."));
        }
    }

    private void executeCommand(Message inputMessage, String command, long chatId) {
        SendMessage outputMessage;
        switch (command) {
            case "/start":
                outputMessage = new SendMessage(chatId, "Hello, enter /create to save your notification");
                break;
            case "/create":
                outputMessage = new SendMessage(chatId, "Enter your notification in format ");
                break;
            default:
                outputMessage = new SendMessage(chatId, "I don't know this command yet");
        }
        try {
            telegramBot.execute(outputMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






