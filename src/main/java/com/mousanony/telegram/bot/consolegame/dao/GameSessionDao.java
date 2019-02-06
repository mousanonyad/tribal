package com.mousanony.telegram.bot.consolegame.dao;

import com.mousanony.telegram.bot.consolegame.session.GameSession;

/**
 * @author sbt-denisov-an
 */
public interface GameSessionDao {
    GameSession getSessionById(long id);
}
