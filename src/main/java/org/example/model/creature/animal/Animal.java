package org.example.model.creature.animal;

import org.example.model.creature.Creature;
import org.example.model.island.IslandCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Animal extends Creature {
    private int movementSpeed;
    private float requiredFood;
    private int maxTicksToStarving;
    private int ticksToStarvingLeft;
    private Map<Class<? extends Creature>, Integer> consumptionTable;

    public Animal(int currentIslandCellX, int currentIslandCellY) {
        setCurrentIslandCellX(currentIslandCellX);
        setCurrentIslandCellY(currentIslandCellY);
        setMaxTicksToStarving(3);
        setTicksToStarvingLeft(3);
        setId(Creature.getMaxId() + 1);
        setMaxId(Creature.getMaxId() + 1);
        System.out.println("~~~" + getId() + "~~~");
    }

    @Override
    public void reproduce(IslandCell islandCell, List<Creature> newborns) {
        ArrayList<Creature> creatures = islandCell.getCreatures();
        boolean sameSpecieAvailable = creatures
                .stream()
                .anyMatch(c -> c != this && c.getClass() == getClass());

        try {
            if (sameSpecieAvailable) {
                if (!checkPopulationFull(islandCell)) {
                    newborns.add(getClass()
                            .getDeclaredConstructor(int.class, int.class)
                            .newInstance(getCurrentIslandCellX(), getCurrentIslandCellY()));
                    System.out.println("New creature " + getClass().getSimpleName() + " (with the id: " + getId() + ") has been born");
                } else {
                    System.out.println(getClass().getSimpleName() + " can not born. There are too many of them!");
                }
            } else {
                System.out.println(getClass().getSimpleName() + " can not born. There creature is alone!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }
    //TODO: implement move method
    public abstract void move();

    public abstract void eat(IslandCell islandCell);

    private boolean checkPopulationFull(IslandCell islandCell) {
        return islandCell.getCreatures().stream().filter(c -> c.getClass() == getClass()).count() == getMaxPopulation();
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        if (movementSpeed > 0) {
            this.movementSpeed = movementSpeed;
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
}
