package com.mousanony.telegram.bot.consolegame.session;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sbt-denisov-an
 */
public class GameSessionTest {
    @Test
    public void rollDiceWithPercent() throws Exception {
        GameSession session = new GameSession(30);
        int percent20 = 0;

        for (int i = 0; i <= 100; i++) {
            if (session.rollDiceWithPercent(20))
                percent20++;
        }

        System.out.println(percent20);
        Assert.assertTrue(percent20 >= 10 && percent20 <= 30);
    }
}