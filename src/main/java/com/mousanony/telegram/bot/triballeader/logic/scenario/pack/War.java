package com.mousanony.telegram.bot.triballeader.logic.scenario.pack;

import com.mousanony.telegram.bot.triballeader.logic.scenario.Situation;
import com.mousanony.telegram.bot.triballeader.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.triballeader.logic.userinteraction.Result;
import com.mousanony.telegram.bot.triballeader.person.Character;
import com.mousanony.telegram.bot.triballeader.person.resources.Resource;
import com.mousanony.telegram.bot.triballeader.session.GameSession;

/**
 * @author mousanonyad
 */
public class War extends Situation {
    public War() {
        super("Тревога! К нам приближается войско соседнего племени," +
                "В войске врага 30 человек. Что будем делать, о великий?");

        addChoice(new Choice("Сформировать армию из простолюдин.",
                new Result() {
                    @Override
                    public String doChange(GameSession logic) {
                        Character character = logic.getCharacter();

                        Resource police = character.getPolice();
                        Resource humans = character.getHumans();
                        //TODO переделать
                        int count = 35 - police.getPositiveValue();
                        int value = humans.getPositiveValue() - count;

                        if (value > 0) {
                            humans.decrease(count);
                            police.increase(count);
                        }
                        //типа если людей не хватает, то берем всех, оставляя только 2х. Херня логика
                        else {
                            humans.decrease(humans.getPositiveValue() - 2);
                            police.increase(humans.getPositiveValue() - 2);
                        }

                        if (police.getPositiveValue() > 25 && logic.rollDice()) {
                            setMessage("Похоже мы победили, о великий, наши ряды пополнены пленными.");
                            police.increaseWithPercent(10);
                            humans.increaseWithPercent(20);
                        } else {
                            setMessage("Мы проиграли это сражение, всё племя перебили.");
                            logic.setGameOver();
                        }
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                //Виден только тогда, когда воинов меньше 30 и людей + воинов больше 30.
                return logic.getCharacter().getHumans().getPositiveValue() + logic.getCharacter().getPolice().getPositiveValue() > 20
                        && logic.getCharacter().getPolice().getPositiveValue() < 30;
            }
        });
        addChoice(new Choice("Использовать военную хитрость и понадеяться на молитвы жрецов.",
                new Result() {
                    @Override
                    public String doChange(GameSession logic) {
                        if (logic.rollDice()) {
                            setMessage("Нам удалось заманить врагов в ловушку и победить." +
                                    "\nЛюди добыли больше еды, лишь бы их мудрый вождь оставался таким же мудрым");
                            logic.getCharacter().getPolice().increaseWithPercent(10);
                            logic.getCharacter().getHumans().increaseWithPercent(10);
                            logic.getCharacter().getFood().increase(20);
                            return super.doChange(logic);
                        }
                        logic.setGameOver();
                        setMessage("Ловушку раскрыли и всё племя перебили.");
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getPriests().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Попробовать откупиться едой.",
                new Result() {
                    @Override
                    public String doChange(GameSession logic) {
                        setMessage("Враг принял наши условия, мы лишились части еды.");
                        logic.getCharacter().getFood().decrease(40);
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getFood().getPositiveValue() > 40;
            }
        });
        addChoice(new Choice("Мы сильнее, предложить им сдаться и присоединиться к нам.",
                new Result() {
                    @Override
                    public String doChange(GameSession logic) {
                        setMessage("Наши ряды пополнились, несогласные принесены в жертву жрецам. Народ ликует.");
                        logic.getCharacter().getPolice().increaseWithPercent(20);
                        logic.getCharacter().getHumans().increaseWithPercent(20);
                        logic.getCharacter().getHumans().increaseWithPercent(20);
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getPolice().getPositiveValue() > 40;
            }
        });
    }
}
