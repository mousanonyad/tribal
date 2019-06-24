package com.mousanony.telegram.bot.consolegame.logic.userinteraction;

import com.mousanony.telegram.bot.consolegame.session.GameSession;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

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

    public @NotNull
    String doChange(GameSession session) {
        String logicResult = session.doLogic().toString();
        if (!StringUtils.isEmpty(logicResult)) {
            return message + "\n\n" + logicResult;
        }
        return message;
    }
}
