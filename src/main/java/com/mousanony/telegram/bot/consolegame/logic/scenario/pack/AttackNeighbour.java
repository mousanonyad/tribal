package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class AttackNeighbour extends Situation {
    public AttackNeighbour() {
        super("Если в соседнем племени бунт, нападём на них?");

        addChoice(new Choice("Да!", new Result() {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                if (session.getCountOfPolice() < 20 || session.rollDiceWithPercent(20)) {
                    setMessage("Да у них бунт, но это не помешало им убить многих наших воинов. Мы отступили..");
                    session.getTribal().getPolice().decreaseWithPercent(70);
                } else {
                    setMessage("Сопротивлявшихся убили, еду забрали.");
                    session.getTribal().getFood().increaseWithPercent(15);
                    session.getTribal().getHumans().increaseWithPercent(15);
                }
                //TODO может переделать потом
                session.deleteSituation(AttackNeighbour.class);
                return super.doChange(session);
            }
        }));
        addChoice(new Choice("Нет", new Result(
                "Ваше решение понравилось вольнодумцам, они принесли в едохранилище больше еды.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().increaseWithPercent(20);
                return super.doChange(session);
            }
        }));
    }
}
