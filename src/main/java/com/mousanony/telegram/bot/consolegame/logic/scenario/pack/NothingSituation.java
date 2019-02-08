package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;

/**
 * @author mousanonyad
 */
public class NothingSituation extends Situation {
    public NothingSituation() {
        super("Сегодня ничего не произошло.");

        addChoice(new Choice("Ладно.", new Result("Солнце светит, люди рождаются..")));
        addChoice(new Choice("Хреново.", new Result("Посмотрим что будет завтра")));
        addChoice(new Choice("Устроить пир.", new Result("Люди довольны и размножаются") {
            @Override
            public String doChange(GameSession session) {
                session.getCharacter().getFood().decreaseWithPercent(20);
                session.getCharacter().getHumans().increaseWithPercent(5);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getFood().getPositiveValue() > 10 && session.rollDice();
            }
        });
    }
}
