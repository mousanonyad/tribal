package com.mousanony.telegram.bot.consolegame.person;

import com.mousanony.telegram.bot.consolegame.person.resources.Resource;

/**
 * @author mousanonyad
 */
public class Character {
    private Resource food;
    private Resource priests;
    private Resource humans;
    private Resource police;

    public Character(Resource food) {
        this.food = food;
        this.priests = new Resource(3);
        this.humans = new Resource(20);
        this.police = new Resource(5);
    }

    public Resource getFood() {
        return food;
    }

    public Resource getPriests() {
        return priests;
    }

    public void setFood(Resource food) {
        this.food = food;
    }

    public void setPriests(Resource priests) {
        this.priests = priests;
    }

    public Resource getHumans() {
        return humans;
    }

    public void setHumans(Resource humans) {
        this.humans = humans;
    }

    public Resource getPolice() {
        return police;
    }

    public void setPolice(Resource police) {
        this.police = police;
    }

    @Override
    public String toString() {
        return "food=" + food.getPositiveValue() +
                ", priests=" + priests.getPositiveValue() +
                ", humans=" + humans.getPositiveValue() +
                ", police=" + police.getPositiveValue();
    }
}
