package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class GiveMePriest extends Situation {
    public GiveMePriest() {
        super("Посланники из соседнего племени просят выделить им двух жрецов для ритуала.");

        addChoice(new Choice("Согласиться.", new Result("Жрецы ушли вместе со своими семьями. Взамен нам дали немного еды.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().decrease(2);
                session.getTribal().getHumans().decrease(6);
                session.getTribal().getFood().increase(25);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPriests() >= 2;
            }
        });
        addChoice(new Choice("Отдать посланников жрецам.", new Result(
                "Боги обрадовались, по крайней мере так сказали жрецы.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().decreaseWithPercent(15);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPriests() > 0;
            }
        });
        addChoice(new Choice("У нас нет жрецов!", new Result("Действительно, вы так мудры!") {
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
        addChoice(new Choice("Отложить решение на потом.", new Result("Они ещё вернутся.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                return super.doChange(session);
            }
        }));
    }
}
