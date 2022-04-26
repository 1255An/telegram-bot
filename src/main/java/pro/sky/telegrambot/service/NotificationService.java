package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.ParsingResults;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@EnableScheduling
@Service
public class NotificationService {
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationTaskRepository notificationTaskRepository;
    private final Pattern pattern = Pattern.compile("(\\d{2}.\\d{2}.\\d{4})\\s(\\d{2}.\\d{2})\\s(\\D)", Pattern.CASE_INSENSITIVE);

    public NotificationService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public NotificationTask findByChatId(long chatId) {
        return notificationTaskRepository.findByChatId(chatId);
    }

    public List<NotificationTask> findAll() {
        return notificationTaskRepository.findAll();
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }

//    @Scheduled(cron = " 0 0/1 * * * *")
//    public void notificationService() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);
//
//        //get notifications list
//        List<NotificationTask> list = notificationTaskRepository.findAll().stream()
//                .filter(notificationTask -> {
//
//                })
//    }

    public ParsingResults parseMessage(String text) {
        logger.info("Method for parsing message was called");
        LocalDateTime localDateTime;
        ParsingResults parsingResults = new ParsingResults();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String dateTime = matcher.group(1) + " " + matcher.group(2);
            logger.info("create datetime");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");
              //  LocalTime time = fixLocalTime(matcher.group(2));
            logger.info("parsing");
                localDateTime = LocalDateTime.parse(dateTime, formatter);
            logger.info("parsed");
            if (localDateTime.isAfter(LocalDateTime.now())) {
                parsingResults.setDateTime(localDateTime);
                parsingResults.setReminderDescription(matcher.group(2).trim().replaceAll("\\s+", " "));
                parsingResults.setStatus("valid");
            } else {
                parsingResults.setStatus("time is in the past");
            }
        } else {
            parsingResults.setStatus("invalid");
        }
        return parsingResults;
    }


//    private LocalTime fixLocalTime(String group) {
//        Character semiColon = group.charAt(1);
//        if(semiColon.equals(' ')){
//            group = '0' + group;
//        }
//        LocalTime time = LocalTime.parse(group);
//        return time;
//    }

//    private LocalDateTime produceLocalDateTime(Matcher matcher) {
//        LocalDateTime dateTime;
//        LocalDate date = LocalDate.now();
//        String gotTime = matcher.group(5);
//
//        LocalTime futureTime = fixLocalTime(gotTime);
//        if (matcher.group(1).equalsIgnoreCase("tomorrow")) {
//            date = date.plusDays(1);
//        }
//        dateTime = LocalDateTime.of(date, futureTime);
//        return dateTime;
//    }
//
//
//    private boolean isShort(String matcherGroup1) {
//        boolean isShort = false;
//        if (matcherGroup1.equalsIgnoreCase("tomorrow") || matcherGroup1.equalsIgnoreCase("today")) {
//            isShort = true;
//        } return isShort;
//    }

    public boolean isNotificationValid(String message) {
        logger.info("Method for checking notification was called");
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }
}


