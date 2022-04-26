package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

//    private NotificationTask nt = new NotificationTask();
    private int stage;


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
            Message message = update.message();
            String command = update.message().text();
            //    long chatId = update.message().chat().id();
            if (message != null && !command.isEmpty()) {
                executeCommand(message, command);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void executeCommand(Message inputMessage, String command) {
        long chatId = inputMessage.chat().id();
        SendMessage outputMessage = null;
        if (command.startsWith("/")) {
            switch (command) {
                case "/start":
                    stage = 0;
                    outputMessage = new SendMessage(chatId, "Hello, enter /create to save your notification");
                    break;
                case "/create":
                    stage = 1;
                    outputMessage = new SendMessage(chatId, "Enter your notification in format ");
                    break;
                default:
                    outputMessage = new SendMessage(chatId, "I don't know this command yet");
                    break;
            }
        } else {
            if (notificationService.isNotificationValid(command)) {
                notificationService.parseMessage(command);
                notificationService.save(command);
            } else {
                outputMessage = new SendMessage(chatId, "Your enter invalid format. Enter /create to ");
            }
        }
        try {
            telegramBot.execute(outputMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private SendMessage applyNotification(Message inputMessage, boolean isApplied, String text) {
//        SendMessage outputMessage;
//        long chatId = inputMessage.chat().id();
//        if (notificationService.isNotificationValid(inputMessage)) {
//            notificationService.parseMessage(inputMessage.toString());
//            notificationService.save(nt);
//            outputMessage = new SendMessage(chatId, text);
//        } else {
//            outputMessage = new SendMessage(chatId, "Wrong");
//        }
//        //     showMenu (outputMessage, "Show new events", "Help");
//        //     stage = 0;
//        return outputMessage;
//    }
//    private void showMenu (SendMessage messageToSend, String... options){
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(new String[]{"first row button1", "first row button2"},
//                new String[]{"second row button1", "second row button2"})
//                .oneTimeKeyboard(true)   // optional
//                .resizeKeyboard(true)    // optional
//                .selective(true);
//
//        List<KeyboardRow> keyboardRows = new ArrayList<>();
//        KeyboardRow firstKeyBoardRow = new KeyboardRow();
//
//        for (String option : options) {
//            firstKeyBoardRow.add(new KeyboardButton(option));
//        }
//
//        keyboardRows.add(firstKeyBoardRow);
//        keyboardMarkup.setKeyboard(keyboardRows);
//    }


}






