package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.ParsingResults;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@EnableScheduling
@Service
public class NotificationService {
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationTaskRepository notificationTaskRepository;
    private final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\w+]+)", Pattern.CASE_INSENSITIVE);

    public NotificationService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public List<NotificationTask> findAll() {
        return notificationTaskRepository.findAll();
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }

    public ParsingResults parseMessage(String text) {
        logger.info("Method for parsing message was called");
        LocalDateTime localDateTime;
        ParsingResults parsingResults = new ParsingResults();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String dateTime = matcher.group(1);
            logger.info("create datetime");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
              //  LocalTime time = fixLocalTime(matcher.group(2));
            logger.info("parsing");
                localDateTime = LocalDateTime.parse(dateTime, formatter);
            logger.info("parsed");
            if (localDateTime.isAfter(LocalDateTime.now())) {
                parsingResults.setDateTime(localDateTime);
                parsingResults.setReminderDescription(matcher.group(3));
                parsingResults.setStatus("valid");
            } else {
                parsingResults.setStatus("time is in the past");
            }
        } else {
            parsingResults.setStatus("invalid");
        }
        return parsingResults;
    }

    @Scheduled
    public void remindAboutEvent () {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List <NotificationTask> tasks = notificationTaskRepository.findAll();

        for (String )
    }


    public boolean isNotificationValid(String message) {
        logger.info("Method for checking notification was called");
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }
}


