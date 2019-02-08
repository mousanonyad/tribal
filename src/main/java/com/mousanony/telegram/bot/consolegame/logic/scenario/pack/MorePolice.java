package com.mousanony.telegram.bot.consolegame.logic.scenario.pack;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Result;
import com.mousanony.telegram.bot.consolegame.session.GameSession;

/**
 * @author mousanonyad
 */
public class MorePolice extends Situation {
    public MorePolice() {
        super("Начальник армии просит набрать людей в воины, заманив их едой.");

        addChoice(new Choice("Отложить идею на потом.", new Result("Военачальник расстроился, но что поделаешь.")));
        addChoice(new Choice("Разрешить, но без еды.", new Result("Нам удалось набрать 10 воинов.") {
            @Override
            public String doChange(GameSession session) {
                session.getCharacter().getHumans().decrease(10);
                session.getCharacter().getPolice().increase(10);
                return super.doChange(session);
            }
        }){
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getHumans().getPositiveValue() > 10;
            }
        });
        addChoice(new Choice("Разрешить.", new Result("Нам удалось набрать 20 воинов.") {
            @Override
            public String doChange(GameSession session) {
                session.getCharacter().getHumans().decrease(20);
                session.getCharacter().getPolice().increase(20);
                return super.doChange(session);
            }
        }){
            @Override
            public boolean isVisible(GameSession session) {
                return session.getCharacter().getHumans().getPositiveValue() > 20;
            }
        });
    }
}
