package com.mousanony.telegram.bot.consolegame.handlers;

import com.mousanony.telegram.bot.consolegame.dao.GameSessionDao;
import com.mousanony.telegram.bot.consolegame.dao.MapGameSessionDao;
import com.mousanony.telegram.bot.consolegame.logic.scenario.Holder;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * @author mousanonyad
 */
public class Bot extends AbilityBot {
    private GameSessionDao gameSessionDao = new MapGameSessionDao();

    public Bot(String botToken, String botUsername, DefaultBotOptions botOptions) {
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
                    Integer id = ctx.user().getId();
                    GameSession session = gameSessionDao.newSessionWithId(id);

                    silent.send(session.getCharacter().toString(), id);
                    //current? где вызывать newHolder?
                    Holder holder = session.getCurrentHolder();

                    try {
                        sender.execute(getMessageWithButtons(ctx, holder));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }

    private String getChoises(Holder holder) {
        //отдаем ответы
        StringBuilder choises = new StringBuilder();
        holder.getVisibleChoises().forEach((k, v) -> choises.append(k).append(". - ").append(v.getMessage()).append("\n"));
        return markDownItalic(choises.toString());
    }

    private SendMessage getMessageWithButtons(MessageContext ctx, Holder holder) {
        return new SendMessage()
                .setChatId(ctx.chatId())
                //как то хз
                .setParseMode(ParseMode.MARKDOWN)
                .setText(markDownBold(holder.getSituation().getMessage()) + "\n\n" + getChoises(holder))
                .setReplyMarkup(withTodayTomorrowButtons(holder.getVisibleChoises()));
    }

    String markDownItalic(String string) {
        return "__" + string + "__";
    }

    String markDownBold(String string) {
        return "*" + string + "*";
    }

    public Ability step() {
        return Ability
                .builder()
                .name(DEFAULT)
                .input(0)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Integer id = ctx.user().getId();
                    GameSession session = gameSessionDao.getSessionById(id);

                    if (session == null || session.isGameOver()) {
                        gameOver(id);
                        return;
                    }

                    //Берем текущую ситуацию и делаем логику
                    Holder holder = session.getCurrentHolder();
                    Choice choice = holder.getVisibleChoises().get(Integer.valueOf(ctx.update().getMessage().getText()));
                    String result = choice.getResult().doChange(session);
                    silent.send(result, id);

                    if (!session.isGameOver()) {
                        silent.send(session.getCharacter().toString(), id);
                        try {
                            sender.execute(getMessageWithButtons(ctx, session.newHolder()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else gameOver(id);
                })
                .build();
    }

    private void gameOver(int id) {
        silent.send("Играем? /start", id);
    }

    private ReplyKeyboard withTodayTomorrowButtons(Map<Integer, Choice> choiceMap) {
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        for (Map.Entry<Integer, Choice> integerChoiceEntry : choiceMap.entrySet()) {
            row.add(integerChoiceEntry.getKey().toString());
        }

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}