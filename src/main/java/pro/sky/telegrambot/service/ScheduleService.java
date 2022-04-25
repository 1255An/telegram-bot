package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrambot.model.NotificationTask;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleService {

    private NotificationService notificationService;

    @Autowired
    private TelegramBot telegramBot;

    public ScheduleService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void scheduleHandler () {
        LocalDateTime now = LocalDateTime.now();
        List<NotificationTask> tasks = notificationService.findAll();
        try {
            for (NotificationTask task : tasks) {
              //  long chatId = .message().chat().id();
                SendMessage message = new SendMessage(task.getChatId(), task.getResponseMessage());
             //   message.setChatId(task.getChatId());
              //  message.setText (task.getDescription());
                telegramBot.execute(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
