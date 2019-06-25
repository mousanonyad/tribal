package com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class LessPolice extends Situation {
    public LessPolice() {
        super("Твои воины попали в засаду патрулируя окрестности. Многие погибли.");

        addChoice(new Choice("Очень, очень расстроен!", new Result("Нужно с этим что-то делать!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPolice().decreaseWithPercent(40);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPolice() > 0;
            }
        });
        addChoice(new Choice("У меня нет воинов!", new Result("Точно, как же вы мудры!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPolice().decreaseWithPercent(40);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPolice() <= 0;
            }
        });
    }
}
