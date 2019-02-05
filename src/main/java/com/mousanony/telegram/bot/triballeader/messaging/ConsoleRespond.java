package com.mousanony.telegram.bot.triballeader.messaging;


/**
 * @author mousanonyad
 */
public class ConsoleRespond implements IRespond {
    @Override
    public void output(String message) {
        System.out.println(message);
    }
}
