package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;

/**
 * @author mousanonyad
 */
public class PoisonedFood extends Situation {
    public PoisonedFood() {
        super("Еда в едохранилище стала мерзко пахнуть и посинела.\nНаверное кто-то из племени отравил часть еды.");

        addChoice(new Choice("Пытать всех возможных участников и добиться от них признания.",
                new Result("Ты нашел всех заговорщиков и они признались, какую именно еду они отравили." +
                        "\nВ пытках погибла значительная часть племени.") {
                    @Override
                    public String doChange(GameSession logic) {
                        logic.getCharacter().getFood().decreaseWithPercent(30);
                        logic.getCharacter().getHumans().decreaseWithPercent(30);
                        return super.doChange(logic);
                    }
                }) {
            @Override
            public boolean isVisible(GameSession logic) {
                return logic.getCharacter().getHumans().getPositiveValue() > 0;
            }
        });
        addChoice(new Choice("Уничтожить запасы возможно отравленной еды.",
                new Result("Тебе удалось предотвратить массовую гибель людей, но и значительный запас еды был уничтожен.") {
                    @Override
                    public String doChange(GameSession logic) {
                        logic.getCharacter().getFood().decreaseWithPercent(logic.getRandomPercent());
                        return super.doChange(logic);
                    }
                }));
        addChoice(new Choice("Ничего не делать. И пусть жрецы молятся.",
                new Result("Большая часть твоих подданых умерла. Народ в панике!") {
                    @Override
                    public String doChange(GameSession logic) {
                        logic.getCharacter().getHumans().decreaseWithPercent(70);
                        logic.getCharacter().getFood().decreaseWithPercent(40);
                        return super.doChange(logic);
                    }
                }) {
        });
    }
}
