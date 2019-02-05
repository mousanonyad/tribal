package com.mousanony.telegram.bot.triballeader.session;

import com.mousanony.telegram.bot.triballeader.logic.scenario.Situation;
import com.mousanony.telegram.bot.triballeader.logic.scenario.pack.Hurricane;
import com.mousanony.telegram.bot.triballeader.logic.scenario.pack.NothingSituation;
import com.mousanony.telegram.bot.triballeader.logic.scenario.pack.PoisonedFood;
import com.mousanony.telegram.bot.triballeader.logic.scenario.pack.War;
import com.mousanony.telegram.bot.triballeader.logic.userinteraction.Choice;
import com.mousanony.telegram.bot.triballeader.messaging.IRespond;
import com.mousanony.telegram.bot.triballeader.messaging.InputHandler;
import com.mousanony.telegram.bot.triballeader.person.Character;
import com.mousanony.telegram.bot.triballeader.person.resources.Resource;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author mousanonyad
 */
public class GameSession {
    private List<Situation> startSituations;
    private List<Situation> middleSituations;
    private List<Situation> holdSituations;
    private Character character;
    private InputHandler inputHandler;
    private Random random;
    private IRespond view;
    private String finalMessage;
    private boolean gameOver = false;
    private AtomicInteger count = new AtomicInteger();

    public GameSession(int humanCount) {
        this.character = new Character(new Resource(humanCount));
        this.inputHandler = new InputHandler();
        this.startSituations = new ArrayList<>();
        this.middleSituations = new ArrayList<>();
        this.holdSituations = new ArrayList<>();
        this.view = view;
        this.random = new Random();

        startSituations.add(new NothingSituation());
        startSituations.add(new PoisonedFood());
        startSituations.add(new Hurricane());

        middleSituations.add(new War());
        startGameSession();
    }

    public void setGameOver() {
        this.gameOver = true;
    }

    public String getFinalMessage() {
        return finalMessage;
    }

    public void setFinalMessage(String finalMessage) {
        this.finalMessage = finalMessage;
    }

    public int getRandomPercent() {
        return random.nextInt(100);
    }

    public Character getCharacter() {
        return character;
    }

    private void startGameSession() {
        while (getCountOfAllPeople() > 0 && !gameOver) {
            doSituationMove();
        }
        view.output("Игра закончена.");
    }

    private void doSituationMove() {
        doLogic();
        if (getCountOfAllPeople() <= 0) {
            return;
        }
        action();
    }

    //TODO зарефакторить и метод и весь класс
    private Choice action() {
        if (count.getAndIncrement() > 2) {
            startSituations.addAll(middleSituations);
        }

        Situation situation = startSituations.get(random.nextInt(startSituations.size()));

        //принт текущее состояние ресурсов
        view.output(character.toString());
        //принт ситуации
        view.output(situation.getMessage());

        //фильтр по видимым ответам
        List<Choice> choiceList = situation.getChoices().stream().filter(p -> p.isVisible(this)).collect(Collectors.toList());
        Collections.shuffle(choiceList);

        //принт ответов
        Map<Integer, Choice> choiceMap = choiceList.stream().collect(Collectors.toMap(choiceList::indexOf, Function.identity()));
        choiceMap.forEach((k, v) -> view.output((k + ". " + v.getMessage())));


        Choice choice = inputHandler.handleInput(choiceMap, view);

        //принт результата и результат
        view.output(choice.getResult().doChange(this));
        return choice;
    }

    private int getCountOfPolice() {
        return character.getPolice().getPositiveValue();
    }

    private int getCountOfPriests() {
        return character.getPriests().getPositiveValue();
    }

    private int getCountOfHumans() {
        return character.getHumans().getPositiveValue();
    }

    private int getCountOfAllPeople() {
        return character.getPolice().getPositiveValue() + character.getPriests().getPositiveValue() + character.getHumans().getPositiveValue();
    }

    private void doLogic() {
        calculateHumans();
        getPercentUnitRelationship();
        calculateResource();
    }

    private void calculateHumans() {
        if (character.getHumans().getPositiveValue() <= 0)
            view.output("Все люди умерли, воинам и жрецам придется самим возделывать землю.");
        if (character.getFood().getPositiveValue() <= 0) {
            character.getHumans().decreaseWithPercent(30);
            character.getPolice().decreaseWithPercent(20);
            character.getPriests().decreaseWithPercent(20);
            view.output("Люди мрут от голода.");
        } else character.getHumans().increaseWithPercent(5);
    }

    private void calculateResource() {
        Resource food = character.getFood();

        if (getCountOfHumans() > 0)
            food.increaseWithPercent(10);
        else
            food.decreaseWithPercent(20);
    }

    private void getPercentUnitRelationship() {
        if (getCountOfHumans() / 100 * getCountOfPolice() > 50) {
            view.output("Воины устраивают дебош и убивают с пытками несколько людей.");
            character.getHumans().decreaseWithPercent(10);
        }
        if (getCountOfHumans() / 100 * getCountOfPriests() > 30) {
            view.output("Народ бастует против засилия жрецов, ест еду и жжет жрецов!");
            character.getPriests().decreaseWithPercent(15);
            character.getFood().decreaseWithPercent(15);
        }
    }

    public boolean rollDice() {
        return Math.random() < 0.5;
    }
}
