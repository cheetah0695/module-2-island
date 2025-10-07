package org.example.model.creature.animal;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.herbivore.Herbivore;
import org.example.model.creature.animal.predator.Predator;
import org.example.model.island.IslandCell;

import java.util.ArrayList;
import java.util.Map;

public abstract class Animal extends Creature {
    private int movementSpeed;
    private float requiredFood;
    private int maxTicksToStarving;
    private int ticksToStarvingLeft;
    private Map<Class<? extends Creature>, Integer> consumptionTable;

    public Animal(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxTicksToStarving(3);
        setTicksToStarvingLeft(3);
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
                    if (!checkPopulationFull(islandCell)) {
                        Creature newCreature = getClass()
                                .getDeclaredConstructor(int.class, int.class)
                                .newInstance(getCurrentIslandCellX(), getCurrentIslandCellY());
                        islandCell.addCreature(newCreature);
                        System.out.println("New creature " + getClass().getSimpleName() + " (with the id: " + newCreature.getId() + ") has been born");
                    } else {
                        System.out.println("New " + getClass().getSimpleName() + " can not be born. There are too many of them!");
                    }
                } else {
                    System.out.println("New " + getClass().getSimpleName() + " can not be born. There creature is alone!");
                }
            } else {
                System.out.println("New " + getClass().getSimpleName() + " can not be born. Parent is dead :(");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }

    public void decrementDaysToStarvation(IslandCell currentIslandCell, Creature prey) {
        setTicksToStarvingLeft(getTicksToStarvingLeft() - 1);

        if (getTicksToStarvingLeft() == 0) {
            currentIslandCell.getCreatures().remove(this);
            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") starved to death :(");
        } else {
            if (this instanceof Predator) {
                if (prey != null) {
                    System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                            ") had unsuccessfully hunted the " + prey.getClass().getSimpleName() +
                            " (id: " + prey.getId() + "). Remaining days to starving: " + getTicksToStarvingLeft());
                } else {
                    System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                            "): had unsuccessfully hunted because there is no prey in the area [" +
                            getCurrentIslandCellX() + "," + getCurrentIslandCellY() + "] .Remaining days to starving: " +
                            getTicksToStarvingLeft());
                }
            }
            if (this instanceof Herbivore) {
                System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                        "): not enough food. Remaining days to starving: " +
                        getTicksToStarvingLeft());
            }
        }
    }
    //TODO: implement move() method
    public void move() {

    }

    public abstract void eat();

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
