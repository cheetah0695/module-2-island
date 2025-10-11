package org.example.model.creature.animal;

import org.example.model.creature.Creature;
import org.example.model.creature.plant.Plant;
import org.example.model.island.Island;
import org.example.model.island.IslandCell;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Creature {
    private int maxMovementRange;
    private float requiredFood;
    private float remainingHunger;
    private int maxTicksToStarving;
    private int ticksToStarvingLeft;
    private Map<Class<? extends Creature>, Integer> consumptionTable;
    private Map<Class<? extends Creature>, Integer> possibleFoodTable;
    boolean atePlantLastTime;

    public Animal(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxTicksToStarving(3);
        setTicksToStarvingLeft(3);
        setAtePlantLastTime(false);
    }

    @Override
    public void reproduce(IslandCell islandCell) {
        ArrayList<Creature> creatures = islandCell.getCreatures();
        boolean sameSpecieAvailable = creatures
                .stream()
                .anyMatch(c -> c != this && c.getClass() == getClass());

        try {
            if (isAlive()) {
                if (sameSpecieAvailable) {
                    if (getRemainingHunger() == 0) {
                        if (!islandCell.checkPopulationFull(this)) {
                            Creature newCreature = getClass()
                                    .getDeclaredConstructor(int.class, int.class)
                                    .newInstance(getCurrentIslandCellX(), getCurrentIslandCellY());
                            islandCell.addCreature(newCreature);
                            System.out.println("New creature " + getClass().getSimpleName() +
                                    " (with the id: " + newCreature.getId() + ") has been born");
                        } else {
                            System.out.println("New " + getClass().getSimpleName() +
                                    " can not be born. There are too many of them!");
                        }
                    } else {
                        System.out.println("New " + getClass().getSimpleName() +
                                " can not be born. The parent is hungry!");
                    }
                } else {
                    System.out.println("New " + getClass().getSimpleName() +
                            " can not be born. There creature is alone!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }

    public void tryToEat(Creature prey) {
        String preyClassName = prey.getClass().getSimpleName();
        boolean wasPreyAlive = prey.isAlive();

        int tableChance = possibleFoodTable.get(prey.getClass());
        int actualChance = ThreadLocalRandom.current().nextInt(0, 101);

        if (!prey.isAlive()) {
            actualChance = 0;
        }

        if (actualChance <= tableChance) {
            if (prey instanceof Plant) {
                setAtePlantLastTime(true);
            } else {
                setAtePlantLastTime(false);
            }

            if (prey.getCurrentWeight() >= getRemainingHunger()) {
                prey.setAlive(false);
                float preyWeight = prey.getCurrentWeight() - getRemainingHunger();
                prey.setCurrentWeight(Math.round(preyWeight * 100) / 100f);

                setTicksToStarvingLeft(getMaxTicksToStarving());
                setRemainingHunger(0);

                String firstPartMessage = getClass().getSimpleName() + " (id: " + getId();
                String howMuchEaten = prey.getCurrentWeight() == 0 ? "completely" : "partly";
                String howMuchFoodLeft = prey.getCurrentWeight() == 0 ? ". " : " " + preyClassName +
                        " food remained: " + prey.getCurrentWeight();
                firstPartMessage += wasPreyAlive ? ") killed and " + howMuchEaten + " ate " : ") " + howMuchEaten +
                        " ate dead ";
                String secondPartMessage = preyClassName + " (id: " + prey.getId() + ")." + howMuchFoodLeft +
                        ". Remaining hunger: " + getRemainingHunger() + ". Days to starvation: " +
                        getTicksToStarvingLeft();
                System.out.println(firstPartMessage + secondPartMessage);
            } else {
                setRemainingHunger(getRemainingHunger() - prey.getCurrentWeight());
                prey.setCurrentWeight(0);
                prey.setAlive(false);
                System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") completely ate " +
                        preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
                        + getTicksToStarvingLeft());
            }
        } else {
            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") failed to hunt " +
                    preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
                    + getTicksToStarvingLeft());
        }
    }

    public void decrementTicksToStarvation() {
        setTicksToStarvingLeft(getTicksToStarvingLeft() - 1);

        if (getTicksToStarvingLeft() == 0) {
            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") starved to death :(");
        }
    }

    public void move() {
        int migrationRange = ThreadLocalRandom.current().nextInt(0, getMaxMovementRange() + 1);

        if (migrationRange == 0) {
            System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                    ") don't want to migrate from the cell: [" + getCurrentIslandCellX() + "," +
                    getCurrentIslandCellY() + "]");
            return;
        }

        for (int i = 1; i <= migrationRange; i++) {
            ArrayList<IslandCell> availableCells = Island
                    .getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY())
                    .findCellsToMigrate(this, getMaxMovementRange());
        }

        //TODO: maybe its better to calculate all available island cells to migrate and then chose among them
    }

    public int getMaxMovementRange() {
        return maxMovementRange;
    }

    public void setMaxMovementRange(int maxMovementRange) {
        if (maxMovementRange > 0) {
            this.maxMovementRange = maxMovementRange;
        }
    }

    public float getRequiredFood() {
        return requiredFood;
    }

    public void setRequiredFood(float requiredFood) {
        if (requiredFood > 0) {
            this.requiredFood = requiredFood;
        }
    }

    public int getMaxTicksToStarving() {
        return maxTicksToStarving;
    }

    public void setMaxTicksToStarving(int maxTicksToStarving) {
        if (maxTicksToStarving > 0) {
            this.maxTicksToStarving = maxTicksToStarving;
        }
    }

    public int getTicksToStarvingLeft() {
        return ticksToStarvingLeft;
    }

    public void setTicksToStarvingLeft(int ticksToStarvingLeft) {
        if (ticksToStarvingLeft >= 0) {
            this.ticksToStarvingLeft = ticksToStarvingLeft;
        }
    }

    public Map<Class<? extends Creature>, Integer> getConsumptionTable() {
        return consumptionTable;
    }

    public void setConsumptionTable(Map<Class<? extends Creature>, Integer> consumptionTable) {
        this.consumptionTable = consumptionTable;
    }

    public float getRemainingHunger() {
        return remainingHunger;
    }

    public void setRemainingHunger(float remainingHunger) {
        if (remainingHunger >= 0 && remainingHunger <= requiredFood) {
            this.remainingHunger = remainingHunger;
        } else if (remainingHunger > requiredFood) {
            this.remainingHunger = requiredFood;
        } else {
            this.remainingHunger = 0;
        }
    }

    public boolean isAtePlantLastTime() {
        return atePlantLastTime;
    }

    public void setAtePlantLastTime(boolean atePlantLastTime) {
        this.atePlantLastTime = atePlantLastTime;
    }

    public Map<Class<? extends Creature>, Integer> getPossibleFoodTable() {
        return possibleFoodTable;
    }

    public void setPossibleFoodTable(Map<Class<? extends Creature>, Integer> possibleFoodTable) {
        this.possibleFoodTable = possibleFoodTable;
    }
}
