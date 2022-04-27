package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@EnableScheduling
public class ScheduleService {

//    private Logger logger = LoggerFactory.getLogger(ScheduleService.class);
//
//    private NotificationService notificationService;
//
//    private AbsSender absSender;
//
//    public ScheduleService(NotificationService notificationService, AbsSender) {
//        this.notificationService = notificationService;
//        this.absSender = absSender;
//    }
//
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void scheduleHandler() {
//        LocalDateTime now = LocalDateTime.now();
//        List<NotificationTask> tasks = notificationService.getNotificationTasks(now.truncatedTo(ChronoUnit.MINUTES), now);
//        logger.info("Got {} tasks to process", tasks.size());
//        try {
//            for (NotificationTask task : tasks) {
//                SendMessage message = new SendMessage(task.getId(), "You have a notification: " + task.getNotificationDescription());
//                logger.info("Message was created with chatId {} and message {}", task.getChatId(), task.getNotificationDescription());
//                //   message.setChatId(task.getChatId());
//                //  message.setText (task.getDescription());
//        //        absSender.execute(message);
//            }
//            } catch(Exception e){
//                logger.info("Failed execution");
//            }
//        }
    }

