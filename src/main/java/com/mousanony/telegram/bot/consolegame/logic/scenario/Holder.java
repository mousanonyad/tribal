package com.mousanony.telegram.bot.consolegame.logic.scenario;

import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.consolegame.session.GameSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sbt-denisov-an
 */
public class Holder {
    private Situation situation;
    private Map<Integer, Choice> visibleChoises;

    public Holder(Situation situation, GameSession gameSession) {
        this.situation = situation;
        this.visibleChoises = getVisibleChoises(situation, gameSession);
    }

    private Map<Integer, Choice> getVisibleChoises(Situation situation, GameSession gameSession) {
        //фильтр по видимым ответам
        List<Choice> choiceList = situation.getChoices().stream().filter(p -> p.isVisible(gameSession)).collect(Collectors.toList());
        Collections.shuffle(choiceList);

        //choiceList.indexOf(o)+1 чтобы индексы были не с нуля, запутывает
        return choiceList.stream().collect(Collectors.toMap(o -> choiceList.indexOf(o) + 1, Function.identity()));
    }

    public Map<Integer, Choice> getVisibleChoises() {
        return visibleChoises;
    }

    public void setVisibleChoises(Map<Integer, Choice> visibleChoises) {
        this.visibleChoises = visibleChoises;
    }

    public Situation getSituation() {
        return situation;
    }
}
