package com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class DecreaseSomeOneSituation extends Situation {
    public DecreaseSomeOneSituation() {
        super("Рядом с кровавой рябиной найдены чьи то останки, кто это был?");

        addChoice(new Choice("Да это же один из мятежников.", new Result("Так точно, это он, после этого пропала ещё часть людей и еды.") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getFood().decreaseWithPercent(10);
                        session.getTribal().getHumans().decreaseWithPercent(20);
                        return super.doChange(session);
                    }
                })
        );
        addChoice(new Choice("Да это же один из воинов.", new Result("Похоже ты угадал.") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getPolice().decrease(1);
                        return super.doChange(session);
                    }
                })
        );
        addChoice(new Choice("Да это же один из жрецов.", new Result("Похоже ты угадал.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().decrease(1);
                return super.doChange(session);
            }
        }));
    }
}
