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
            public String doChange(GameSession logic) {
                logic.getCharacter().getFood().decreaseWithPercent(20);
                logic.getCharacter().getHumans().increaseWithPercent(5);
                return super.doChange(logic);
            }
        }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getFood().getPositiveValue() > 10 && logic.rollDice();
            }
        });
    }
}
