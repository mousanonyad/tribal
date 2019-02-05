package com.mousanony.telegram.bot.triballeader.logic.userinteraction;

import com.mousanony.telegram.bot.triballeader.session.GameSession;

/**
 * @author mousanonyad
 */
public class Result {
    private String message;

    public Result(String message) {
        this.message = message;
    }

    public Result() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String doChange(GameSession logic) {
        return message;
    }
}
