package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class NeighbourImmigration extends Situation {
    public NeighbourImmigration() {
        super("Перебежчики из соседнего племени говорят у них бунт, и просят стать частью нашего племени.");

        addChoice(new Choice("Принять их и отправить возделывать землю.", new Result() {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                if (session.rollDiceWithPercent(20)) {
                    setMessage("Оказалось это шпионы! Они сожгли наше едохранилище и скрылись!");
                    session.getTribal().getFood().decreaseWithPercent(80);
                } else {
                    setMessage("Они оказались полезными, к тому же указали нам нетронутые угодья винограда.");
                    session.getTribal().getFood().increaseWithPercent(20);
                    session.getTribal().getHumans().increase(10);
                    //Добавляем ситуацию с нападением
                    session.setLeftOffSituations(new AttackNeighbour());
                }
                return super.doChange(session);
            }
        }));
        addChoice(new Choice("Да они шпионы, отдадим их жрецам!", new Result(
                "Жрецы обрадовались, красное пламя восславит наших богов!") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                //TODO сделать какие последствия?
                return super.doChange(session);
            }
        }));
        addChoice(new Choice("У нас мало еды, давайте съедим их!", new Result(
                "Не все согласились с таким шагом, мы отправили их к нашим богам.") {
            @NotNull
            @Override
            public String doChange(GameSession session) {
                session.getTribal().getHumans().decrease(10);
                session.getTribal().getFood().increase(50);

                //Добавляем ситуацию с нападением
                session.setLeftOffSituations(new AttackNeighbour());

                return super.doChange(session);
            }
        }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getFood().getPositiveValue() <= 60;
            }
        });
    }
}
