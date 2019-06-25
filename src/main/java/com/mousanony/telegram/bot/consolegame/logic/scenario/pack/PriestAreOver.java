package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class PriestAreOver extends Situation {
    public PriestAreOver() {
        super("Народ взмолился, у них нет жрецов, без них они не знают зачем жить!");

        addChoice(new Choice("Пригласить жрецов из соседнего племени.", new Result(
                "Жрецы согласились, но пришлось поделиться едой.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().increase(6);
                session.getTribal().getFood().decreaseWithPercent(30);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getFood().getPositiveValue() > 10;
            }
        });
        addChoice(new Choice("Отправить часть народа на обучение.", new Result(
                "Некоторые обучились, а некоторые не выдержали и были принесены в жертву богам.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {

                    session.getTribal().getPriests().increase(5);
                    session.getTribal().getHumans().decrease(7);

                return super.doChange(session);
            }
        }){
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfHumans() >= 5;
            }
        });
        addChoice(new Choice("Отправить часть воинов на обучение.", new Result(
                "Часть воинов смогли принять новое учение. Еще столько же умерло от мозговых усилий.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().increase(5);
                session.getTribal().getPolice().decrease(10);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPolice() >= 10;
            }
        });
        addChoice(new Choice("Отложить решение на потом.", new Result(
                "Люди устроили в едохранилище промискуитет. Самые усердные стали жрецами!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().decreaseWithPercent(20);
                session.getTribal().getHumans().decrease(2);
                session.getTribal().getPriests().increase(2);
                return super.doChange(session);
            }
        }));
    }
}
