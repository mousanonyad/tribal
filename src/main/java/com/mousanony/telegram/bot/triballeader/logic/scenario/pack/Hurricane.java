package com.mousanony.telegram.bot.triballeader.logic.scenario.pack;

import com.mousanony.telegram.bot.triballeader.logic.scenario.Situation;
import com.mousanony.telegram.bot.triballeader.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.triballeader.logic.userinteraction.Result;
import com.mousanony.telegram.bot.triballeader.session.GameSession;

/**
 * @author mousanonyad
 */
public class Hurricane extends Situation {
    public Hurricane() {
        super("Прошел мощный ураган, часть хижин пострадала.");

        addChoice(new Choice("Разместить людей в едохранилищах.",
                new Result("Пришлось выкинуть часть еды!") {
                    @Override
                    public String doChange(GameSession logic) {
                        logic.getCharacter().getFood().decreaseWithPercent(30);
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getFood().getPositiveValue() > 10;
            }
        });
        addChoice(new Choice("Разместить людей в почти пустых едохранилищах.",
                new Result("Хорошо, что у нас мало еды, правда ведь?") {
                    @Override
                    public String doChange(GameSession logic) {
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getFood().getPositiveValue() < 10;
            }
        });
        addChoice(new Choice("Ничего не делать. И пусть жрецы молятся.",
                new Result("Большая часть твоих подданых умерла. Народ в панике!") {
                    @Override
                    public String doChange(GameSession logic) {
                        logic.getCharacter().getHumans().decreaseWithPercent(80);
                        logic.getCharacter().getFood().decreaseWithPercent(40);
                        return super.doChange(logic);
                    }
                }) {
        });
        addChoice(new Choice("Разместить людей у жрецов.",
                new Result() {
                    @Override
                    public String doChange(GameSession logic) {
                        if (logic.getCharacter().getPriests().getPositiveValue() > 3) {
                            if (logic.rollDice()) {
                                setMessage("Жрецы не очень обрадовались. Куда-то пропала часть людей.");
                                logic.getCharacter().getHumans().decreaseWithPercent(10);
                            } else setMessage("Всё прошло спокойно, люди спасены.");
                        } else {
                            setMessage("Несколько человек остались в учениках у жрецов.");
                            logic.getCharacter().getPriests().increaseWithPercent(10);
                            logic.getCharacter().getHumans().decreaseWithPercent(10);
                        }

                        return super.doChange(logic);
                    }
                }) {
        });
    }
}
