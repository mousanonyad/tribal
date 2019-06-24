package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class MorePolice extends Situation {
    public MorePolice() {
        super("Военачальник просит набрать людей в воины, заманив их едой.");

        addChoice(new Choice("Отложить идею на потом.", new Result(
                "Военачальник расстроился, но что поделаешь.")));
        addChoice(new Choice("Разрешить, но без еды.", new Result(
                "Нам удалось набрать 5 воинов, к сожалению некоторые люди отказались и были убиты.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getHumans().decrease(12);
                session.getTribal().getPolice().increase(5);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getHumans().getPositiveValue() > 10;
            }
        });
        addChoice(new Choice("Разрешить.", new Result("Нам удалось набрать 10 воинов.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getHumans().decrease(10);
                session.getTribal().getFood().decrease(30);
                session.getTribal().getPolice().increase(10);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfHumans() > 15 && session.getTribal().getFood().getPositiveValue() > 30;
            }
        });
    }
}
