package com.mousanony.telegram.bot.consolegame.session;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Holder;
import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.Hurricane;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.NothingSituation;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.PoisonedFood;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.War;
import com.mousanony.telegram.bot.consolegame.person.Character;
import com.mousanony.telegram.bot.consolegame.person.resources.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mousanonyad
 */
public class GameSession {
    private List<Situation> startSituations;
    private List<Situation> middleSituations;
    private List<Situation> holdSituations;
    private Holder holder;
    private Character character;
    private Random random;
    private String finalMessage;
    private boolean gameOver = false;
    private AtomicInteger count = new AtomicInteger();

    public GameSession(int humanCount) {
        this.character = new Character(new Resource(humanCount));
        this.startSituations = new ArrayList<>();
        this.middleSituations = new ArrayList<>();
        this.holdSituations = new ArrayList<>();
        this.random = new Random();

        startSituations.add(new NothingSituation());
        startSituations.add(new PoisonedFood());
        startSituations.add(new Hurricane());

        middleSituations.add(new War());
        holder = newHolder();
    }

    public Holder getCurrentHolder() {
        return holder;
    }

    public Holder newHolder() {
        if (count.getAndIncrement() > 2) {
            startSituations.addAll(middleSituations);
        }
        Situation situation = startSituations.get(random.nextInt(startSituations.size()));
        holder = new Holder(situation, this);
        return holder;
    }

    public void setGameOver() {
        this.gameOver = true;
        holder = null;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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

    public StringBuilder doLogic() {
        StringBuilder builder = new StringBuilder();
        if (isGameOver())
            return builder;
        calculateHumans(builder);
        getPercentUnitRelationship(builder);
        calculateResource();

        if (getCountOfAllPeople() <= 0){
            setGameOver();
            builder.append("Все умерли.");
        }

        return builder;
    }

    private void calculateHumans(StringBuilder builder) {
        if (character.getHumans().getPositiveValue() <= 0)
            builder.append("Все люди умерли, воинам и жрецам придется самим возделывать землю.\n");
        if (character.getFood().getPositiveValue() <= 0) {
            character.getHumans().decreaseWithPercent(30);
            character.getPolice().decreaseWithPercent(20);
            character.getPriests().decreaseWithPercent(20);
            builder.append("Люди мрут от голода.\n");
        } else character.getHumans().increaseWithPercent(5);
    }

    private void calculateResource() {
        Resource food = character.getFood();
        if (getCountOfHumans() > 0)
            food.increaseWithPercent(10);
        else
            food.decreaseWithPercent(20);
    }

    private void getPercentUnitRelationship(StringBuilder builder) {
        if (getCountOfHumans() / 100 * getCountOfPolice() > 50) {
            builder.append("Воины устраивают дебош и убивают с пытками несколько людей.\n");
            character.getHumans().decreaseWithPercent(10);
        }
        if (getCountOfHumans() / 100 * getCountOfPriests() > 30) {
            builder.append("Народ бастует против засилия жрецов, ест еду и жжет жрецов!\n");
            character.getPriests().decreaseWithPercent(15);
            character.getFood().decreaseWithPercent(15);
        }
    }

    public boolean rollDice() {
        return Math.random() < 0.5;
    }
}
