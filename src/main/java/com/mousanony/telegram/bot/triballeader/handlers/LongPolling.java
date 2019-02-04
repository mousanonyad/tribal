package com.mousanony.telegram.bot.triballeader.handlers;

import com.mousanony.telegram.bot.triballeader.TelegramRespond;
import com.mousanony.telegram.bot.triballeader.config.BotConfig;
import com.mousanonyad.session.GameSession;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

/**
 * @author mousanonyad
 */
public class LongPolling extends TelegramLongPollingBot {

    private Map<Long, GameSession> gameSessionMap;

    @Override
    public void onUpdateReceived(Update update) {
        GameSession gameSession;
        if (update.hasMessage() && update.getMessage().hasText()) {

            Message updateMessage = update.getMessage();
            Long chatId = updateMessage.getChatId();
            if (gameSessionMap.containsKey(chatId)) {
                gameSession = gameSessionMap.get(chatId);
            } else {
                gameSession = new GameSession(30, new TelegramRespond());
                gameSessionMap.put(chatId, gameSession);
            }



            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(chatId)
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {

    }

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

}