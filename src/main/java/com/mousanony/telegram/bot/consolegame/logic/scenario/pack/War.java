package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class War extends Situation {
    public War(int countOfAttackers, int countOfFood) {
        super("Тревога! К нам приближается войско соседнего племени. " +
                "В войске врага " + countOfAttackers + " человек. Что будем делать, о великий?");


        addChoice(new Choice("Использовать военную хитрость и понадеяться на ритуалы жрецов.",
                new Result() {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        if (session.rollDiceWithPercent(40)) {
                            setMessage("Нам удалось заманить врагов в ловушку и победить.");
                            createWar(15 + session.getRandom().nextInt(10), 50 + session.getRandom().nextInt(15), session);
                            return super.doChange(session);
                        }
                        session.setGameOver();
                        setMessage("Ловушку раскрыли и всё племя перебили.");
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getPriests().getPositiveValue() > 2 &&
                        session.getTribal().getHumans().getPositiveValue() > 20;
            }
        });
        addChoice(new Choice("Попробовать откупиться едой.",
                new Result() {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        setMessage("Враг принял наши условия, мы лишились части еды.");
                        session.getTribal().getFood().decrease(countOfFood);

                        createWar(30 + session.getRandom().nextInt(15), 80 + session.getRandom().nextInt(20), session);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getFood().getPositiveValue() >= countOfFood;
            }
        });
        addChoice(new Choice("Сражаться!",
                new Result() {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        //TODO придумать нормальную логику
                        if (session.getTribal().getPolice().getPositiveValue() +
                                session.getTribal().getHumans().getPositiveValue() > countOfAttackers * 2) {
                            setMessage("Это была тяжелая битва, но мы победили!");
                            session.getTribal().getPolice().decreaseWithPercent(60);
                            session.getTribal().getHumans().decreaseWithPercent(30);
                            createWar(session.getRandom().nextInt(28), session.getRandom().nextInt(50), session);
                        } else if (session.getCountOfPolice() > countOfAttackers) {
                            session.getTribal().getPolice().decreaseWithPercent(50);
                            setMessage("Мы победили!");
                            createWar(session.getRandom().nextInt(20), session.getRandom().nextInt(45), session);
                        } else {
                            if (session.rollDice()) {
                                setMessage("Мы проиграли.");
                                session.setGameOver();
                            } else
                                setMessage("Нападавших забрали боги на круглых летающих тарелках..");
                        }
                        return super.doChange(session);
                    }
                }));
        addChoice(new Choice("Сдаться.",
                new Result() {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.setGameOver();
                        if (session.rollDiceWithPercent(30))
                            setMessage("Игра окончена. Но племя, которое на нас напало, процветает.");
                        setMessage("Игра окончена.");
                        return super.doChange(session);
                    }
                }));
    }

    private void createWar(int countOfAttackers, int countOfFood, GameSession session) {
        session.deleteSituation(War.class);
        session.getStartSituations().add(new War(countOfAttackers, countOfFood));
    }
}
