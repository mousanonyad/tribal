package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class NothingSituation extends Situation {
    public NothingSituation() {
        super("Сегодня ничего не произошло.");

        addChoice(new Choice("Ладно.", new Result("Солнце светит, люди рождаются..")));
        addChoice(new Choice("Хреново.", new Result("Посмотрим что будет завтра.")));
        addChoice(new Choice("Устроить пир.", new Result("Люди довольны и размножаются.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().decreaseWithPercent(30);
                session.getTribal().getHumans().increaseWithPercent(2);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getFood().getPositiveValue() > 10 && session.rollDice();
            }
        });
    }
}
