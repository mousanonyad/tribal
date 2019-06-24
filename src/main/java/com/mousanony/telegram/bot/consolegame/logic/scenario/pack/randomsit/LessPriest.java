package com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class LessPriest extends Situation {
    public LessPriest() {
        super("Один жрец сошёл с ума.");

        addChoice(new Choice("Очень, очень расстроен!", new Result("Нужно с этим что-то сделать!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().decrease(1);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPriests() > 0;
            }
        });
        addChoice(new Choice("У меня нет жрецов!", new Result("Да, ты прав. Похоже это чужой жрец!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPriests() <= 0;
            }
        });
    }
}
