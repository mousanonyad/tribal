package com.mousanony.telegram.bot.consolegame.handlers;

import com.mousanony.telegram.bot.consolegame.dao.GameSessionDao;
import com.mousanony.telegram.bot.consolegame.dao.MapGameSessionDao;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * @author mousanonyad
 */
public class LongPolling extends AbilityBot {
    private static final String BOT_NAME = "TribalLeaderBot";
    private static final String BOT_TOKEN = "token";
    private GameSessionDao gameSessionDao = new MapGameSessionDao();

    public LongPolling(String botToken, String botUsername, DefaultBotOptions botOptions) {
        super(botToken, botUsername, botOptions);
    }

    @Override
    public int creatorId() {
        return 11274465;
    }

    public Ability sayHelloWorld() {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action(ctx -> silent.send("Hello world!", ctx.chatId()))
                .post(ctx -> silent.send("Bye world!", ctx.chatId()))
                .build();
    }

    public Ability startGame() {
        return Ability
                .builder()
                .name("start")
                .info("Начать игру.")
                .input(0)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    silent.send("Здесь будет вводный инструктаж.", ctx.chatId());
                    Map<String, GameSession> gameSessionMap = db.getMap("GameSessionMap");

                    Integer id = ctx.user().getId();
                    GameSession session = new GameSession(30);
                    gameSessionMap.put(String.valueOf(id), session);
                    silent.send(session.getCharacter().toString(), id);
                    silent.send(session.getCurrentSittuation().getMessage(), id);
                })
                .post(ctx -> silent.send("И здесь что-то будет.", ctx.chatId()))
                .build();
    }
}