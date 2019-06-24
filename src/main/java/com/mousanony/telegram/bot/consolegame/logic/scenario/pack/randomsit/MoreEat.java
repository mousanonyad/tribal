package com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class MoreEat extends Situation {
    public MoreEat() {
        super("Утром мы нашли корзины с едой, наверное боги поощряют нас за наши дела!");

        addChoice(new Choice("Ура!", new Result("Как раз то что нужно!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().increase(20);
                return super.doChange(session);
            }
        }));
    }
}
