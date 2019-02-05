package com.mousanony.telegram.bot.triballeader.dao;

import com.mousanony.telegram.bot.triballeader.session.GameSession;

/**
 * @author sbt-denisov-an
 */
public interface GameSessionDao {
    GameSession getSessionById(long id);
}
