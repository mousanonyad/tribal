package com.mousanony.telegram.bot.consolegame.messaging;

import com.mousanony.telegram.bot.consolegame.logic.userinteraction.Choice;

import java.util.Map;
import java.util.Scanner;

/**
 * @author mousanonyad
 */
public class InputHandler implements IHandler {
    private static final String WRONG_INPUT = "Нет, попробуй снова, я жду цифру.";
    private Scanner scanner = new Scanner(System.in);

    public Choice handleInput(Map<Integer, Choice> collect, IRespond respond) {
        while (true) {
            try {
                Choice choice = collect.get(Integer.parseInt(scanner.nextLine()));
                if (choice != null)
                    return choice;
                else respond.output("Нет такого варианта.");
            } catch (NumberFormatException e) {
                respond.output(WRONG_INPUT);
            }
        }
    }
}
