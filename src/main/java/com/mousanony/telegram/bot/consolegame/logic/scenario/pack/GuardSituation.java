package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class GuardSituation extends Situation {
    public GuardSituation() {
        super("Старейшина просит выделить немного воинов на охрану \"ущелья гласа\" от хищников.");
        addChoice(new Choice("Отложить решение.", new Result("Хищники погрызли и запасы и людей.") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getFood().decreaseWithPercent(15);
                        session.getTribal().getHumans().decreaseWithPercent(10);
                        session.getTribal().getPolice().decreaseWithPercent(5);
                        session.getTribal().getPriests().decrease(2);
                        return super.doChange(session);
                    }
                })
        );
        addChoice(new Choice("Позволить воинам делать своё дело.", new Result("Воины сделали своё дело. А мясо пригодно в пищу!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().increase(30);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getPolice().getPositiveValue() > 35;
            }
        });
        addChoice(new Choice("Разрешить.", new Result("Несколько смельчаков покинули отряд воинов и теперь находятся в распоряжении старейшины. Часть еды была выделена для похода к пещере.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getFood().decrease(10);
                session.getTribal().getPolice().decrease(5);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getPolice().getPositiveValue() > 10;
            }
        });
    }
}
