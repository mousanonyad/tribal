package com.mousanony.telegram.bot.triballeader.messaging;


import com.mousanony.telegram.bot.triballeader.logic.userinteraction.Choice;

import java.util.Map;

/**
 * @author mousanonyad
 */
public interface IHandler {
    Choice handleInput(Map<Integer, Choice> collect, IRespond respond);
}
