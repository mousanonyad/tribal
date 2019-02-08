package com.mousanony.telegram.bot.consolegame.logic.userinteraction;

import com.mousanony.telegram.bot.consolegame.session.GameSession;

/**
 * @author mousanonyad
 */
public class Choice {
    private Result result;
    private String message;

    public Choice(String message, Result result) {
        this.message = message;
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public boolean isVisible(GameSession session) {
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
