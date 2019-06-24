package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class MorePriest extends Situation {
    public MorePriest() {
        super("Жрецам приснился человеко-жук. Теперь они просят провести сложное испытание и выбрать учеников из народа.");

        addChoice(new Choice("Провести ограниченный набор.", new Result("Теперь у нас ещё два жреца.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().increase(3);
                session.getTribal().getHumans().decrease(3);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPriests() > 0;
            }
        });
        addChoice(new Choice("Запретить.", new Result("Жрецы так расстроились, что часть еды куда-то пропала.") {
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
        addChoice(new Choice("Разрешить испытание.", new Result("Нам удалось набрать 10 жрецов, для испытания потребовалось много еды, очень много еды.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getHumans().decrease(10);
                session.getTribal().getPriests().increase(10);
                session.getTribal().getFood().decreaseWithPercent(30);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfHumans() > 20 && session.getCountOfPriests() > 0;
            }
        });
        addChoice(new Choice("У меня нет жрецов!", new Result("Да, ты прав. Похоже это чужой жрец!") {
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
    }
}
