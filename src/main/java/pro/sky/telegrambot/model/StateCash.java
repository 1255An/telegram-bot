package pro.sky.telegrambot.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//переключатель состояния бота
@Service
public class StateCash {

    private final Map<Long, State> stateMap = new HashMap<>();

    public StateCash() {
    }
    public Map<Long, State> getStateMap() {
        return stateMap;
    }
    public void saveState (Long chatId, State state) {
        stateMap.put(chatId,state);
    }
}
