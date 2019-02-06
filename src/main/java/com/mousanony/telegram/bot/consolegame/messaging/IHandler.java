package com.mousanony.telegram.bot.consolegame.messaging;


import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;

import java.util.Map;

/**
 * @author mousanonyad
 */
public interface IHandler {
    Choice handleInput(Map<Integer, Choice> collect, IRespond respond);
}
