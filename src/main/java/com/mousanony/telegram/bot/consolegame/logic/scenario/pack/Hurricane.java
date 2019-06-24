package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class Hurricane extends Situation {
    public Hurricane() {
        super("Прошел мощный ураган, часть хижин пострадала.");

        addChoice(new Choice("Разместить людей в едохранилищах.",
                new Result("Пришлось выкинуть часть еды!") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getFood().decreaseWithPercent(30);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getFood().getPositiveValue() > 10 && session.getTribal().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Разместить людей в почти пустых едохранилищах.",
                new Result("Хорошо, что у нас мало еды, правда ведь?") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getFood().getPositiveValue() < 10 && session.getTribal().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Ничего не делать. И пусть жрецы молятся.",
                new Result("Большая часть твоих подданых умерла. Народ в панике!") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getHumans().decreaseWithPercent(80);
                        session.getTribal().getFood().decreaseWithPercent(40);
                        session.getTribal().getPolice().decreaseWithPercent(30);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Разместить людей у жрецов.",
                new Result() {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        if (session.getTribal().getPriests().getPositiveValue() > 3) {
                            if (session.rollDice()) {
                                setMessage("Жрецы не очень обрадовались. Куда-то пропала часть людей.");
                                session.getTribal().getHumans().decreaseWithPercent(10);
                            } else setMessage("Всё прошло спокойно, люди спасены.");
                        } else {
                            setMessage("Несколько человек остались в учениках у жрецов.");
                            session.getTribal().getPriests().increaseWithPercent(10);
                            session.getTribal().getHumans().decreaseWithPercent(10);
                        }

                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("У меня нет людей, здорово, правда?",
                new Result("Нет людей, нет проблем.") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getHumans().getPositiveValue() <= 0;
            }
        });
    }
}
