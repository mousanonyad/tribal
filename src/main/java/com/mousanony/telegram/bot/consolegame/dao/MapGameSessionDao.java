package com.mousanony.telegram.bot.consolegame.dao;

import com.mousanony.telegram.bot.consolegame.session.GameSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbt-denisov-an
 */
public class MapGameSessionDao implements GameSessionDao {
    private Map<Long, GameSession> gameSessionMap;

    public MapGameSessionDao() {
        gameSessionMap = new HashMap<>();
    }

    @Override
    public GameSession getSessionById(long id) {
        return gameSessionMap.get(id);
    }

    @Override
    public GameSession newSessionWithId(long id) {
        gameSessionMap.put(id, new GameSession(30));
        return gameSessionMap.get(id);
    }
}
