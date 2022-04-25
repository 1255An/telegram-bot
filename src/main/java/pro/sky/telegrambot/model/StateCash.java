package pro.sky.telegrambot.model;

import java.util.HashMap;
import java.util.Map;

//переключатель состояния бота
public class StateCash {
    private final Map<Long, State> stateMap = new HashMap<>();

    public void saveState (Long chatId, State state) {
        stateMap.put(chatId,state);
    }
}
