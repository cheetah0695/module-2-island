package org.example.model.creature.animal;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.predator.Predator;
import org.example.model.creature.plant.Plant;
import org.example.model.island.Island;
import org.example.model.island.IslandCell;
import org.example.model.island.Migration;

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
        maxTicksToStarving = config.getInt("island.max-ticks-to-starving");
        setTicksToStarvingLeft(config.getInt("island.max-ticks-to-starving"));
        atePlantLastTime = false;
        movedThisTick = false;
    }

    @Override
    public void run() {
        if (isAlive()) {
            runEat();
            handleRemainingHunger();
            reproduce();
            runMigrate();
            setHungerToMax();
        } else if (Island.getTick() > getWasKilledOnTick()) {
            handleRotting();
        }
    }

    @Override
    public synchronized void reproduce() {
        IslandCell islandCell = Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());

        ArrayList<Creature> creatures = islandCell.getCreatures();
        boolean sameSpecieAvailable = creatures
                .stream()
                .anyMatch(c -> c != this && c.getClass() == getClass());

        try {
            if (isAlive()) {
                if (sameSpecieAvailable) {
                    if (getRemainingHunger() == 0) {
                        synchronized (islandCell) {
                            if (!islandCell.isPopulationFull(this)) {
                                Creature newCreature = getClass()
                                        .getDeclaredConstructor(int.class, int.class)
                                        .newInstance(getCurrentIslandCellX(), getCurrentIslandCellY());
                                islandCell.addCreature(newCreature);
//                                System.out.println("New creature " + getClass().getSimpleName() +
//                                        " (with the id: " + newCreature.getId() + ") has been born");
                            } else {
//                                System.out.println("New " + getClass().getSimpleName() +
//                                        " can not be born. There are too many of them!");
                            }
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

    public void runEat() {
        IslandCell currentIslandCell = Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());

        if (this.getRemainingHunger() == 0) {
//            System.out.println(this.getClass().getSimpleName() + " (id: " + this.getId() +
//                    ") refuses to hunt because it doesn't need food right now"
//            );
            return;
        }

        Creature prey = currentIslandCell.getPrey(this);
        if (prey != null && this.isAlive()) {
            synchronized (prey) {
                synchronized (currentIslandCell) {
                    if (this instanceof Predator || !(prey instanceof Plant)) {
                        this.tryToEat(prey);
                        if (prey.getCurrentWeight() == 0) {
                            currentIslandCell.removeCreature(prey);
                        }
                    } else {
                        while (this.getRemainingHunger() > 0 && prey instanceof Plant) {
                            if (prey.getCurrentWeight() == 0) {
                                prey = currentIslandCell.getPrey(this);
                            }

                            if (prey != null) {
                                this.tryToEat(prey);
                                if (prey.getCurrentWeight() == 0) {
                                    currentIslandCell.removeCreature(prey);
                                }
                            }
                        }
                    }
                }
            }
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
            this.atePlantLastTime = prey instanceof Plant;
            prey.setAlive(false);

            if (prey.getWasKilledOnTick() == -1) {
                prey.setWasKilledOnTick(Island.getTick());
            }

            if (prey.getCurrentWeight() >= getRemainingHunger()) {
                float preyWeight = prey.getCurrentWeight() - getRemainingHunger();
                prey.setCurrentWeight(Math.round(preyWeight * 100) / 100f);

                setTicksToStarvingLeft(this.maxTicksToStarving);
                setRemainingHunger(0);

                String firstPartMessage = getClass().getSimpleName() + " (id: " + getId();
                String howMuchEaten = prey.getCurrentWeight() == 0 ? "completely" : "partly";
                String howMuchFoodLeft = prey.getCurrentWeight() == 0 ? ". " : " " + preyClassName +
                        " food remained: " + prey.getCurrentWeight();
                firstPartMessage += wasPreyAlive ? ") killed and " + howMuchEaten + " ate " : ") " + howMuchEaten +
                        " ate dead ";
                String secondPartMessage = preyClassName + " (id: " + prey.getId() + ")." + howMuchFoodLeft +
                        ". Remaining hunger: " + getRemainingHunger() + ". Days to starvation: " +
                        ticksToStarvingLeft;
//                System.out.println(firstPartMessage + secondPartMessage);
//                System.out.println("ANIMAL ALIVE: " + this.isAlive());
            } else {
                setRemainingHunger(getRemainingHunger() - prey.getCurrentWeight());
                prey.setCurrentWeight(0);
//                System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") completely ate " +
//                        preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
//                        + ticksToStarvingLeft);
//                System.out.println("ANIMAL ALIVE: " + this.isAlive());
            }
        } else {
//            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") failed to hunt " +
//                    preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
//                    + ticksToStarvingLeft);
        }
    }

    public void decrementTicksToStarvation() {
        setTicksToStarvingLeft(ticksToStarvingLeft - 1);

//        if (ticksToStarvingLeft == 0) {
//            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") starved to death :(");
//        }
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

    public Map<Class<? extends Creature>, Integer> getPossibleFoodTable() {
        return possibleFoodTable;
    }

    public boolean isAtePlantLastTime() {
        return atePlantLastTime;
    }

    protected void setPossibleFoodTable(Map<Class<? extends Creature>, Integer> possibleFoodTable) {
        this.possibleFoodTable = possibleFoodTable;
    }

    private synchronized void handleRemainingHunger() {
        IslandCell islandCell = Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
        synchronized (islandCell) {
            if ((this.getRemainingHunger() > this.getRequiredFood() / 2) && this.isAlive()) {
                this.decrementTicksToStarvation();

                if (this.ticksToStarvingLeft == 0) {
                    this.setAlive(false);

                    if (this.getWasKilledOnTick() == -1) {
                        this.setWasKilledOnTick(Island.getTick());
                    }
                }
            }
        }
    }

    private synchronized void runMigrate() {
        Migration migration = getMigration();
        applyMigration(migration);
        movedThisTick = false;
    }

    private Migration createMigration() {
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

            this.movedThisTick = true;

            return new Migration(Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY()), newCell);
        }
    }

    private Migration getMigration() {
        if (!movedThisTick && this.isAlive()) {
            Migration migration = this.createMigration();

            if (migration != null) {
                return migration;
            }
        }
        return null;
    }

    private void applyMigration(Migration migration) {
        if (migration != null) {
            IslandCell currentIslandCell = migration.getCurrentIslandCell();
            IslandCell newIslandCell = migration.getNewIslandCell();

            if (newIslandCell != currentIslandCell) {
                IslandCell firstLock = currentIslandCell.getId() < newIslandCell.getId() ?
                        currentIslandCell : newIslandCell;
                IslandCell secondLock = currentIslandCell.getId() < newIslandCell.getId() ?
                        newIslandCell : currentIslandCell;

                synchronized (firstLock) {
                    synchronized (secondLock) {
                        this.setCurrentIslandCellX(newIslandCell.getX());
                        this.setCurrentIslandCellY(newIslandCell.getY());
                        newIslandCell.addCreature(this);
                        currentIslandCell.removeCreature(this);
                    }
                }
            }
        }
    }

    private void setHungerToMax() {
        this.setRemainingHunger(getRequiredFood());
    }

    private void setTicksToStarvingLeft(int ticksToStarvingLeft) {
        if (ticksToStarvingLeft >= 0) {
            this.ticksToStarvingLeft = ticksToStarvingLeft;
        }
    }
}
