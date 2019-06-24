package com.mousanony.telegram.bot.consolegame.session;

import com.mousanony.telegram.bot.consolegame.logic.scenario.Holder;
import com.mousanony.telegram.bot.consolegame.logic.scenario.Situation;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.*;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit.DecreaseSomeOneSituation;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit.LessPolice;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit.LessPriest;
import com.mousanony.telegram.bot.consolegame.logic.scenario.pack.randomsit.MoreEat;
import com.mousanony.telegram.bot.consolegame.person.Tribal;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mousanonyad
 */
public class GameSession {
    private List<Situation> startSituations;
    private List<Situation> middleSituations;
    private List<Situation> randomSituations;
    private List<Situation> leftOffSituations;
    private Deque<Situation> situationDeque;
    private Holder holder;
    private Tribal tribal;
    private Random random;
    private String finalMessage;
    private boolean gameOver = false;
    private AtomicInteger count = new AtomicInteger();

    public GameSession(int foodCount) {
        this.tribal = new Tribal(foodCount);
        this.startSituations = new ArrayList<>();
        this.middleSituations = new ArrayList<>();
        this.randomSituations = new ArrayList<>();
        this.leftOffSituations = new ArrayList<>();
        this.random = new Random();

        startSituations.add(new NothingSituation());
        startSituations.add(new PoisonedFood());
        startSituations.add(new Hurricane());
        startSituations.add(new MorePolice());
        startSituations.add(new GuardSituation());
        startSituations.add(new MorePriest());
        startSituations.add(new PriestFire());
        startSituations.add(new War(25 + random.nextInt(10), 75 + random.nextInt(10)));
        startSituations.add(new NeighbourImmigration());
        startSituations.add(new GiveMePriest());
        Collections.shuffle(startSituations);

//        Collections.shuffle(middleSituations);

        situationDeque = new ArrayDeque<>();
        situationDeque.addAll(startSituations);
//        situationDeque.addAll(middleSituations);

//        startSituations.addAll(middleSituations);

        randomSituations.add(new LessPolice());
        randomSituations.add(new MoreEat());
        randomSituations.add(new DecreaseSomeOneSituation());
        randomSituations.add(new LessPriest());

        holder = newHolder();
    }

    public Random getRandom() {
        return random;
    }

    public List<Situation> getStartSituations() {
        return startSituations;
    }

    public void setLeftOffSituations(Situation situation) {
        this.leftOffSituations.add(situation);
    }

    public Holder getCurrentHolder() {
        return holder;
    }

    public Holder newHolder() {
        count.incrementAndGet();
        if (situationDeque.isEmpty()) {
            Collections.shuffle(startSituations);
            situationDeque.addAll(startSituations);
        }

        Situation situation;
        if (rollDiceWithPercent(13)) {
            situation = randomSituations.get(random.nextInt(randomSituations.size()));
            holder = new Holder(situation, this);
            return holder;
        }

        //типа отложенные ситуации, если есть, то добавляем
        if (!leftOffSituations.isEmpty()) {
            //TODO ну хз, типа всё вначало
            situationDeque.push(leftOffSituations.get(0));
            leftOffSituations.remove(0);
        }

        situation = situationDeque.poll();
        holder = new Holder(situation, this);
        return holder;
    }

    public void setGameOver() {
        this.gameOver = true;
        holder = null;
        finalMessage = "Ходов: " + count.get() + ".";
    }

    public void deleteSituation(Class situationClass) {
        for (Situation s : startSituations) {
            //TODO что я делаю, о ужас
            if (Objects.equals(s.getClass().getSimpleName(), situationClass.getSimpleName())) {
                startSituations.remove(s);
                break;
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
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

    public Tribal getTribal() {
        return tribal;
    }

    public int getCountOfPolice() {
        return tribal.getPolice().getPositiveValue();
    }

    public int getCountOfPriests() {
        return tribal.getPriests().getPositiveValue();
    }

    public int getCountOfHumans() {
        return tribal.getHumans().getPositiveValue();
    }

    public int getCountOfAllPeople() {
        return tribal.getPolice().getPositiveValue() + tribal.getPriests().getPositiveValue() + tribal.getHumans().getPositiveValue();
    }

    public StringBuilder doLogic() {
        StringBuilder builder = new StringBuilder();
        if (isGameOver())
            return builder;
        calculateHumans(builder);
        getPercentUnitRelationship(builder);

        if (getCountOfAllPeople() <= 0) {
            setGameOver();
            builder.append("Все умерли.");
        }

        return builder;
    }

    private void calculateHumans(StringBuilder builder) {
        if (tribal.getHumans().getPositiveValue() <= 0)
            builder.append("Все люди умерли, воинам и жрецам придется самим возделывать землю.\n");
        if (tribal.getFood().getPositiveValue() <= 0) {
            tribal.getHumans().decreaseWithPercent(30);
            tribal.getPolice().decreaseWithPercent(20);
            tribal.getPriests().decreaseWithPercent(20);
            builder.append("Племя мрет от голода.\n");
        } else tribal.getHumans().increaseWithPercent(2);
    }

    private void getPercentUnitRelationship(StringBuilder builder) {
        //если воинов слишком много
        if (getCountOfPolice() / ((float) getCountOfHumans() / 100) > 50) {
            tribal.getFood().decreaseWithPercent(20);
            //если простых людей больше 60%
        } else if (getCountOfHumans() / ((float) getCountOfAllPeople() / 100) > 60) {
            tribal.getFood().increaseWithPercent(10);
        } else tribal.getFood().decreaseWithPercent(15);

        //если жрецов слишком много
        if (getCountOfPriests() / ((float) getCountOfHumans() / 100) > 40 && getCountOfPriests() > 10) {
            builder.append("Народ бастует против засилия жрецов, ест еду и жжет жрецов!\n");
            tribal.getPriests().decreaseWithPercent(30);
            tribal.getFood().decreaseWithPercent(20);
        }

        //если закончились жрецы
        if (getCountOfPriests() <= 0 && getCountOfHumans() > 0) {
            leftOffSituations.add(new PriestAreOver());
        }

        if (getTribal().getFood().getPositiveValue() > getCountOfAllPeople() && getTribal().getFood().getPositiveValue() > 170) {
            builder.append("Часть еды сгнила, ничего не поделаешь!");
            getTribal().getFood().decreaseWithPercent(10);
        }
    }

    public boolean rollDice() {
        return Math.random() < 0.5;
    }

    public boolean rollDiceWithPercent(int percent) {
        return random.nextFloat() <= (float) percent / 100;
    }
}
