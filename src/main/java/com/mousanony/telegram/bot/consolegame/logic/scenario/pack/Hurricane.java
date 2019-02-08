package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;

/**
 * @author mousanonyad
 */
public class Hurricane extends Situation {
    public Hurricane() {
        super("Прошел мощный ураган, часть хижин пострадала.");

        addChoice(new Choice("Разместить людей в едохранилищах.",
                new Result("Пришлось выкинуть часть еды!") {
                    @Override
                    public String doChange(GameSession session) {
                        session.getCharacter().getFood().decreaseWithPercent(30);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getFood().getPositiveValue() > 10 && session.getCharacter().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Разместить людей в почти пустых едохранилищах.",
                new Result("Хорошо, что у нас мало еды, правда ведь?") {
                    @Override
                    public String doChange(GameSession session) {
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getFood().getPositiveValue() < 10 && session.getCharacter().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Ничего не делать. И пусть жрецы молятся.",
                new Result("Большая часть твоих подданых умерла. Народ в панике!") {
                    @Override
                    public String doChange(GameSession session) {
                        session.getCharacter().getHumans().decreaseWithPercent(80);
                        session.getCharacter().getFood().decreaseWithPercent(40);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Разместить людей у жрецов.",
                new Result() {
                    @Override
                    public String doChange(GameSession session) {
                        if (session.getCharacter().getPriests().getPositiveValue() > 3) {
                            if (session.rollDice()) {
                                setMessage("Жрецы не очень обрадовались. Куда-то пропала часть людей.");
                                session.getCharacter().getHumans().decreaseWithPercent(10);
                            } else setMessage("Всё прошло спокойно, люди спасены.");
                        } else {
                            setMessage("Несколько человек остались в учениках у жрецов.");
                            session.getCharacter().getPriests().increaseWithPercent(10);
                            session.getCharacter().getHumans().decreaseWithPercent(10);
                        }

                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("У меня нет людей, здорово, правда?",
                new Result("Нет людей, нет проблем.") {
                    @Override
                    public String doChange(GameSession session) {
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getHumans().getPositiveValue() <= 0;
            }
        });
    }
}
