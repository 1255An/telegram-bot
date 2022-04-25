package pro.sky.telegrambot.Handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.model.State;
import pro.sky.telegrambot.model.StateCash;

public class MessageHandler {
    private final StateCash stateCash;

    public MessageHandler(StateCash stateCash) {
        this.stateCash = stateCash;
    }

    public void handleMessage (Message message, State state) {
        long chatId = message.chat().id();
        SendMessage sendMessage = new SendMessage(chatId, message.text());
        stateCash.saveState(chatId, state);
        switch (state.name()) {
            case ("ENTER_REMINDER") :
                return eventHandler.enterNotification(message, chatId);
        }
    }
}
