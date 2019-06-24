package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author mousanonyad
 */
public class PoisonedFood extends Situation {
    public PoisonedFood() {
        super("Еда в едохранилище стала мерзко пахнуть и посинела. Наверное кто-то из племени отравил часть еды.");

        addChoice(new Choice("Пытать всех возможных участников и добиться от них признания.",
                new Result("Ты нашел всех заговорщиков и они признались, какую именно еду они отравили." +
                        "В пытках погибла значительная часть племени.") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getFood().decreaseWithPercent(30);
                        session.getTribal().getHumans().decreaseWithPercent(30);
                        return super.doChange(session);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession session) {
                return session.getTribal().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Уничтожить запасы возможно отравленной еды.",
                new Result("Тебе удалось предотвратить массовую гибель людей, но и значительный запас еды был уничтожен.") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        int randomPercent = session.getRandomPercent();
                        //не меньше 20% TODO переделать бы
                        if (randomPercent < 20)
                            randomPercent = 20;
                        session.getTribal().getFood().decreaseWithPercent(randomPercent);
                        return super.doChange(session);
                    }
                }));
        addChoice(new Choice("Ничего не делать. И пусть жрецы молятся.",
                new Result("Большая часть твоих подданых умерла. Народ в панике!") {
                    @NotNull
                    @Override
                    public String doChange(GameSession session) {
                        session.getTribal().getHumans().decreaseWithPercent(70);
                        session.getTribal().getPolice().decreaseWithPercent(30);
                        session.getTribal().getPriests().decreaseWithPercent(25);
                        session.getTribal().getFood().decreaseWithPercent(40);
                        return super.doChange(session);
                    }
                }) {
        });
    }
}
