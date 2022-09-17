package pro.sky.telegrambot.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.ParsingResults;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class NotificationService {
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationTaskRepository notificationTaskRepository;
    // create needed format of notification for checking and parsing users' messages
    private final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\w+]+)", Pattern.CASE_INSENSITIVE);

    public NotificationService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }

    /**
     * Method for getting notification from repository
     */

    public List<NotificationTask> getNotificationTasks(LocalDateTime from, LocalDateTime to) {
        List<NotificationTask> tasks = notificationTaskRepository.getNotificationTasks(from, to);
        logger.info("Getting tasks from {} to {}: [{}]", from, to, tasks);
        if (!tasks.isEmpty()) {
            for (NotificationTask task : tasks) {
                // receive notification description from repository
                String notificationDescription = task.getNotificationDescription();
                task.setNotificationDescription(notificationDescription);
            }
        }
        return tasks;
    }

    /**
     * Parsing received notification to further saving in DB
     */
    public ParsingResults parseMessage(String text) {
        logger.info("Method for parsing message was called");
        LocalDateTime localDateTime;
        ParsingResults parsingResults = new ParsingResults();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String dateTime = matcher.group(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            try {
                localDateTime = LocalDateTime.parse(dateTime, formatter);
                logger.info("User's message was parsed");
                //set valid status of parsing if date isn't in the past
                if (localDateTime.isAfter(LocalDateTime.now())) {
                    parsingResults.setDateTime(localDateTime);
                    parsingResults.setNotificationDescription(matcher.group(3));
                    parsingResults.setStatus("valid");
                } else {
                    parsingResults.setStatus("time is in the past");
                }
            } catch (DateTimeParseException e) {
                parsingResults.setStatus("incorrect dateTime");
            }
        } else {
            parsingResults.setStatus("invalid");
        }

        return parsingResults;
    }
}


