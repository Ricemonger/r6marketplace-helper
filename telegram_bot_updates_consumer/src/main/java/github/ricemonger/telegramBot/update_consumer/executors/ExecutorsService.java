package github.ricemonger.telegramBot.update_consumer.executors;

import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
@RequiredArgsConstructor
public class ExecutorsService {

    private final BotInnerService botInnerService;

    public <T extends AbstractBotCommandExecutor> void execute(Class<T> executor, UpdateInfo updateInfo) {
        try {
            T executorInstance = executor.getDeclaredConstructor().newInstance();
            executorInstance.initAndExecute(updateInfo, botInnerService);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
