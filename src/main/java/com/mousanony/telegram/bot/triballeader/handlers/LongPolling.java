package com.mousanony.telegram.bot.triballeader.handlers;

import com.mousanony.telegram.bot.triballeader.dao.GameSessionDao;
import com.mousanony.telegram.bot.triballeader.dao.MapGameSessionDao;
import com.mousanony.telegram.bot.triballeader.session.GameSession;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author mousanonyad
 */
public class LongPolling extends TelegramLongPollingBot {
    private static final String BOT_NAME = "TribalLeaderBot";
    private static final String BOT_TOKEN = "token";
    private GameSessionDao gameSessionDao = new MapGameSessionDao();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            Message updateMessage = update.getMessage();
            Long chatId = updateMessage.getChatId();

            GameSession session = gameSessionDao.getSessionById(chatId);

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
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}