package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.ParsingResults;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    /**
     * Method for receiving replies from the bot whenever we send our message
     * @param updates used for receiving and checking different types of updates
     */

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // check if the update has a message and message has text
            if (update.message() != null && update.message().text() != null) {
                String command = update.message().text();
                long chatId = update.message().chat().id();
                if (command.startsWith("/")) {
                    executeCommand(command, chatId);
                } else {
                    handleMessage(update, chatId);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Method for recognition and implementation of posted command
     */

    private void executeCommand(String command, long chatId) {
        SendMessage outputMessage;
        switch (command) {
            case "/start":
                outputMessage = new SendMessage(chatId, "Hello, type /create to set your notification");
                break;
            case "/create":
                outputMessage = new SendMessage(chatId, "Type your event in format \nDD.MM.YYYY HH:MM TEXT");
                break;
            case "/help":
                outputMessage = new SendMessage(chatId, "Type DATE TIME DESCRIPTION of your event in format DD.MM.YYYY HH:MM TEXT (e.g. 01.01.2023 15:00 Bob's birthday), and I'll notify you:)");
                break;
            default:
                outputMessage = new SendMessage(chatId, "Sorry, I don't know this command yet.\nType /create to set your notification!");
        }
        try {
            telegramBot.execute(outputMessage);
        } catch (Exception e) {
            logger.info("Exception was thrown in execution command method ");
            e.printStackTrace();
        }
    }

    /**
     * Method for handling and saving in DB received notification
     */

    private void handleMessage(Update update, long chatId) {
        String userMessage = update.message().text();
        long id = update.message().chat().id();
        ParsingResults parsingResults = notificationService.parseMessage(userMessage);
        //save received notification in DB if it has valid format
        if (parsingResults.getStatus().equals("valid")) {
            String responseMessage = parsingResults.getNotificationDescription();
            NotificationTask notificationTask = new NotificationTask(id, chatId, responseMessage,
                    parsingResults.getDateTime());
            notificationService.save (notificationTask);
            logger.info("Notification task was successfully saved");
            telegramBot.execute(new SendMessage(chatId, "Great! I'll notify you about your event"));
        } else if (parsingResults.getStatus().equals("time is in the past")) {
            telegramBot.execute(new SendMessage(chatId, "Entered time has already passed. Please, type your event again, or type /help and I'll help you"));
        } else {
            telegramBot.execute(new SendMessage(chatId, "Format of event should be \n DD.MM.YYYY HH:MM TEXT"));
        }
    }

    /**
     * Method for sending notifications to users
     */

    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduleHandler() {
        LocalDateTime now = LocalDateTime.now();
        List<NotificationTask> tasks = notificationService.getNotificationTasks(now.truncatedTo(ChronoUnit.MINUTES), now);
        logger.info("Got {} tasks to process", tasks.size());
        try {
            for (NotificationTask task : tasks) {
                SendMessage message = new SendMessage(task.getChatId(), "You have a notification: " + task.getNotificationDescription() + ". Don't forget!");
                logger.info("Message was created with chatId {} and message {}", task.getChatId(), task.getNotificationDescription());
                telegramBot.execute(message);
            }
        } catch (Exception e) {
            logger.info ("Exception was thrown in schedule handling method");
            throw new RuntimeException(e);
        }
    }
}






