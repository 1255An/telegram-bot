package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.ParsingResults;
import pro.sky.telegrambot.service.NotificationService;
import pro.sky.telegrambot.service.ScheduleService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;
    private final ScheduleService scheduleService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService, ScheduleService scheduleService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
        this.scheduleService = scheduleService;
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
        String userMessage = update.message().text();
        long id = update.message().chat().id();
        //     SendMessage message = new SendMessage(update.message().chat().id(), userMessage)
        ParsingResults parsingResults = notificationService.parseMessage(userMessage);
        if (parsingResults.getStatus().equals("valid")) {
            String responseMessage = parsingResults.getNotificationDescription();
            NotificationTask notificationTask = new NotificationTask(id, chatId, responseMessage,
                    parsingResults.getDateTime());
            notificationService.save(notificationTask);
            logger.info("Saved");
            telegramBot.execute(new SendMessage(chatId, "Success!"));
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

    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduleHandler() {
        LocalDateTime now = LocalDateTime.now();
        List<NotificationTask> tasks = notificationService.getNotificationTasks(now.truncatedTo(ChronoUnit.MINUTES), now);
        logger.info("Got {} tasks to process", tasks.size());
        for (NotificationTask task : tasks) {
            SendMessage message = new SendMessage(task.getChatId(), "You have a notification: " + task.getNotificationDescription());
            logger.info("Message was created with chatId {} and message {}", task.getChatId(), task.getNotificationDescription());
            telegramBot.execute(message);
        }
    }
}






