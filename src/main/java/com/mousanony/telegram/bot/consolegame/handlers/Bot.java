package com.mousanony.telegram.bot.consolegame.handlers;

import com.mousanony.telegram.bot.consolegame.dao.GameSessionDao;
import com.mousanony.telegram.bot.consolegame.dao.MapGameSessionDao;
import com.mousanony.telegram.bot.consolegame.logic.scenario.Holder;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.person.Tribal;
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
                    silent.send("Ты вождь племени, у тебя есть люди, воины и жрецы." +
                            "Твоя задача выжить и стать успешным племенем.", ctx.chatId());
                    Integer id = ctx.user().getId();
                    GameSession session = gameSessionDao.newSessionWithId(id);

                    silent.send(printResources(session.getTribal()), id);
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

    private String printResources(Tribal tribal) {
        return "\uD83D\uDC6A " + tribal.getHumans().getPositiveValue() +
                "     \uD83D\uDC73 " + tribal.getPriests().getPositiveValue() +
                "     \uD83D\uDC6E " + tribal.getPolice().getPositiveValue() +
                "     \uD83C\uDF56 " + tribal.getFood().getPositiveValue();

    }

    private String getChoices(Holder holder) {
        //отдаем ответы
        StringBuilder choices = new StringBuilder();
        holder.getVisibleChoices().forEach((k, v) -> choices.append(k).append(". - ").append(v.getMessage()).append("\n"));
        return choices.toString();
    }

    private SendMessage getMessageWithButtons(MessageContext ctx, Holder holder) {
        return new SendMessage()
                .setChatId(ctx.chatId())
                //как то хз
                .setParseMode(ParseMode.MARKDOWN)
                .setText(markDownBold(holder.getSituation().getMessage()) + "\n\n" + getChoices(holder))
                .setReplyMarkup(withTodayTomorrowButtons(holder.getVisibleChoices()));
    }

    String markDownItalic(String string) {
        return "_" + string + "_";
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
                        gameOver(id, session);
                        return;
                    }

                    //Берем текущую ситуацию и делаем логику
                    Holder holder = session.getCurrentHolder();
                    Choice choice = holder.getVisibleChoices().get(Integer.valueOf(ctx.update().getMessage().getText()));

                    //Если нет такого ответа
                    if (choice == null) {
                        silent.send("Не понял.", id);
                    } else {
                        String result = choice.getResult().doChange(session);
                        silent.sendMd(markDownItalic(result), id);

                        if (!session.isGameOver()) {
                            silent.send(printResources(session.getTribal()), id);
                            try {
                                sender.execute(getMessageWithButtons(ctx, session.newHolder()));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        } else gameOver(id, session);
                    }
                })
                .build();
    }

    private void gameOver(int id, GameSession session) {
        if (session != null && session.getFinalMessage() != null) {
            silent.send(session.getFinalMessage(), id);
            session.setFinalMessage(null);
        }
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