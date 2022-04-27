package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrambot.model.NotificationTask;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class ScheduleService {

    private Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private NotificationService notificationService;

    private final TelegramBot telegramBot;

    public ScheduleService(NotificationService notificationService, TelegramBot telegramBot) {
        this.notificationService = notificationService;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron ="0 0/1 * * * *")
    public void scheduleHandler () {
        LocalDateTime now = LocalDateTime.now();
        List<NotificationTask> tasks = notificationService.getNotificationTasks(now.truncatedTo(ChronoUnit.MINUTES), now);
        logger.info("Got {} tasks to process", tasks.size());
        //     try {
            for (NotificationTask task : tasks) {
              //  long chatId = .message().chat().id();
                SendMessage message = new SendMessage(task.getChatId(), "You have a notification: " + task.getResponseMessage());
                logger.info("Message was created");
             //   message.setChatId(task.getChatId());
              //  message.setText (task.getDescription());
                telegramBot.execute(message);
            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
