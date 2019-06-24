package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class PriestFire extends Situation {
    public PriestFire() {
        super("Во время ритуала загорелось едохранилище.");

        addChoice(new Choice("Послать людей тушить.", new Result(
                "Людям удалось потушить пожар. Еда пострадала, некоторые погибли.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getHumans().decreaseWithPercent(15);
                session.getTribal().getFood().decreaseWithPercent(15);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfHumans() > 10;
            }
        });
        addChoice(new Choice("Послать людей тушить.", new Result(
                "Людям удалось потушить пожар. Жрецов нет, а пожары есть, хм..") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getHumans().decreaseWithPercent(15);
                session.getTribal().getFood().decreaseWithPercent(20);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfHumans() > 10 && session.getCountOfPriests() <= 0;
            }
        });
        addChoice(new Choice("Послать жрецов тушить.", new Result(
                "Жрецы потушили пожар, не без помощи духов. Еда пострадала незначительно.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getPriests().decreaseWithPercent(40);
                session.getTribal().getFood().decreaseWithPercent(15);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPriests() > 0;
            }
        });
        addChoice(new Choice("Послать воинов тушить.", new Result(
                "Воины потушили пожар, куда-то пропала пара жрецов. Еда пострадала незначительно.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                // ну и ладно, если жрецов нет %%%
                session.getTribal().getPriests().decrease(2);
                session.getTribal().getFood().decreaseWithPercent(15);
                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCountOfPolice() > 0;
            }
        });
        addChoice(new Choice("Подождать дождя.", new Result() {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                if (session.rollDiceWithPercent(30)) {
                    setMessage("Нам повезло, случился дождь и потушил пожар.");
                } else {
                    setMessage("Все сгорело, начнём сначала?");
                    session.setGameOver();
                }
                return super.doChange(session);
            }
        }));
    }
}
