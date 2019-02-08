package com.mousanony.telegram.bot.consolegame.logic.scenario;


import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mousanonyad
 */
public abstract class Situation {
    private final String message;
    private Set<Choice> choices;

    public Situation(String message) {
        this.choices = new HashSet<>();
        this.message = message;
    }

    Set<Choice> getChoices() {
        return choices;
    }

    protected void addChoice(Choice choice) {
        this.choices.add(choice);
    }

    public String getMessage() {
        return message;
    }
}
