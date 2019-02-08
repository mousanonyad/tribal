package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.person.Character;
import com.mousanony.telegram.bot.consolegame.person.resources.Resource;
import com.mousanony.telegram.bot.consolegame.session.GameSession;

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
                    public String doChange(GameSession session) {
                        Character character = session.getCharacter();

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


                        if (police.getPositiveValue() > 30 || police.getPositiveValue() > 20 && session.rollDice()) {
                            setMessage("Похоже мы победили, о великий, наши ряды пополнены пленными.");
                            police.increaseWithPercent(10);
                            humans.increaseWithPercent(20);
                        } else {
                            setMessage("Мы проиграли это сражение, всё племя перебили.");
                            session.setGameOver();
                        }
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                //Виден только тогда, когда воинов меньше 30 и людей + воинов больше 30.
                return session.getCharacter().getHumans().getPositiveValue() + session.getCharacter().getPolice().getPositiveValue() > 20
                        && session.getCharacter().getPolice().getPositiveValue() < 30;
            }
        });
        addChoice(new Choice("Использовать военную хитрость и понадеяться на молитвы жрецов.",
                new Result() {
                    @Override
                    public String doChange(GameSession session) {
                        if (session.rollDice()) {
                            setMessage("Нам удалось заманить врагов в ловушку и победить." +
                                    "\nЛюди добыли больше еды, лишь бы их мудрый вождь оставался таким же мудрым");
                            session.getCharacter().getPolice().increaseWithPercent(10);
                            session.getCharacter().getHumans().increaseWithPercent(10);
                            session.getCharacter().getFood().increase(20);
                            return super.doChange(session);
                        }
                        session.setGameOver();
                        setMessage("Ловушку раскрыли и всё племя перебили.");
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getPriests().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Попробовать откупиться едой.",
                new Result() {
                    @Override
                    public String doChange(GameSession session) {
                        setMessage("Враг принял наши условия, мы лишились части еды.");
                        session.getCharacter().getFood().decrease(40);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getFood().getPositiveValue() > 40;
            }
        });
        addChoice(new Choice("Мы сильнее, предложить им сдаться и присоединиться к нам.",
                new Result() {
                    @Override
                    public String doChange(GameSession session) {
                        setMessage("Наши ряды пополнились, несогласные принесены в жертву жрецам. Народ ликует.");
                        session.getCharacter().getPolice().increaseWithPercent(20);
                        session.getCharacter().getHumans().increaseWithPercent(20);
                        session.getCharacter().getHumans().increaseWithPercent(20);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getPolice().getPositiveValue() > 40;
            }
        });
    }
}
