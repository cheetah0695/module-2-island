package org.example.model.creature.animal;

import org.example.model.creature.Creature;
import org.example.model.creature.plant.Plant;
import org.example.model.island.Island;
import org.example.model.island.IslandCell;
import org.example.model.island.Migration;
import org.example.utils.Config;

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
    private boolean atePlantLastTime;
    private boolean movedThisTick;

    public Animal(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxTicksToStarving(3);
        setTicksToStarvingLeft(3);
        setAtePlantLastTime(false);
        setMovedThisTick(false);
    }

    @Override
    public synchronized void reproduce(IslandCell islandCell) {
        ArrayList<Creature> creatures = islandCell.getCreatures();
        boolean sameSpecieAvailable = creatures
                .stream()
                .anyMatch(c -> c != this && c.getClass() == getClass());

        try {
            if (isAlive()) {
                if (sameSpecieAvailable) {
                    if (getRemainingHunger() == 0) {
                        if (!islandCell.isPopulationFull(this)) {
                            Creature newCreature = getClass()
                                    .getDeclaredConstructor(int.class, int.class)
                                    .newInstance(getCurrentIslandCellX(), getCurrentIslandCellY());
                            islandCell.addCreature(newCreature);
//                            System.out.println("New creature " + getClass().getSimpleName() +
//                                    " (with the id: " + newCreature.getId() + ") has been born");
                        } else {
//                            System.out.println("New " + getClass().getSimpleName() +
//                                    " can not be born. There are too many of them!");
                        }
                    } else {
//                        System.out.println("New " + getClass().getSimpleName() +
//                                " can not be born. The parent is hungry!");
                    }
                } else {
//                    System.out.println("New " + getClass().getSimpleName() +
//                            " can not be born. There creature is alone!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + this.getClass().getSimpleName(), e);
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
            setAtePlantLastTime(prey instanceof Plant);

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
//                System.out.println(firstPartMessage + secondPartMessage);
//                System.out.println("ANIMAL ALIVE: " + this.isAlive());
            } else {
                setRemainingHunger(getRemainingHunger() - prey.getCurrentWeight());
                prey.setCurrentWeight(0);
                prey.setAlive(false);
//                System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") completely ate " +
//                        preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
//                        + getTicksToStarvingLeft());
//                System.out.println("ANIMAL ALIVE: " + this.isAlive());
            }
        } else {
//            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") failed to hunt " +
//                    preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
//                    + getTicksToStarvingLeft());
        }
    }

    public void decrementTicksToStarvation() {
        setTicksToStarvingLeft(getTicksToStarvingLeft() - 1);

//        if (getTicksToStarvingLeft() == 0) {
//            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") starved to death :(");
//        }
    }

    public Migration createMigration() {
        IslandCell currentCell = Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
        ArrayList<IslandCell> cellsToMigrate = new ArrayList<>();

        if (this != null) {
            cellsToMigrate = currentCell.findCellsToMigrate(this);
        }

        if (cellsToMigrate.isEmpty()) {
//            System.out.println(this.getClass().getSimpleName() + ": no available cells to migrate!");

            return null;
        }

        int randomCellIndex = ThreadLocalRandom.current().nextInt(0, cellsToMigrate.size());
        IslandCell newCell = cellsToMigrate.get(randomCellIndex);

        if (newCell == currentCell) {
//            System.out.println(this.getClass().getSimpleName() + " decided to stay in the current cell [" +
//                    this.getCurrentIslandCellX() + "," + this.getCurrentIslandCellY() + "]");

            return null;
        } else {
//            System.out.println(this.getClass().getSimpleName() + " (id: " + this.getId() + ")" +
//                    " have migrated from cell [" + this.getCurrentIslandCellX() + "," +
//                    this.getCurrentIslandCellY() + "] to [" + newCell.getX() + "," + newCell.getY() + "]");

            this.setMovedThisTick(true);

            return new Migration(
                    this, Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY()), newCell
            );
        }
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

    public boolean isMovedThisTick() {
        return movedThisTick;
    }

    public void setMovedThisTick(boolean movedThisTick) {
        this.movedThisTick = movedThisTick;
    }
}
