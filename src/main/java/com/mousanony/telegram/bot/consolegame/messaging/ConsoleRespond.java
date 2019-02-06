package com.mousanony.telegram.bot.consolegame.messaging;


/**
 * @author mousanonyad
 */
public class ConsoleRespond implements IRespond {
    @Override
    public void output(String message) {
        System.out.println(message);
    }
}
