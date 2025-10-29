package org.example.model.creature;

import org.example.model.island.Island;
import org.example.model.island.IslandCell;
import org.example.utils.Config;
import org.example.utils.IdGeneratorUtil;

public abstract class Creature implements Runnable {
    private long id;
    private float maxWeight;
    private float currentWeight;
    private int currentIslandCellX;
    private int currentIslandCellY;
    private int maxPopulation;
    private boolean isAlive;
    private int ticksToRotting;
    private int wasKilledOnTick;

    protected static Config config = Config.getInstance();

    protected Creature(int currentIslandCellX, int currentIslandCellY) {
        this.id = IdGeneratorUtil.nextId();
        isAlive = true;
        setCurrentIslandCellX(currentIslandCellX);
        setCurrentIslandCellY(currentIslandCellY);
        setTicksToRotting(config.getInt("island.max-ticks-to-rotting"));
        setWasKilledOnTick(-1);
    }

    public void run() {
    }

    public abstract void tryToEat(Creature creature);

    public abstract void reproduce();

    public long getId() {
        return id;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getCurrentIslandCellX() {
        return currentIslandCellX;
    }

    public void setCurrentIslandCellX(int currentIslandCellX) {
        this.currentIslandCellX = currentIslandCellX;
    }

    public int getCurrentIslandCellY() {
        return currentIslandCellY;
    }

    public void setCurrentIslandCellY(int currentIslandCellY) {
        this.currentIslandCellY = currentIslandCellY;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public void setMaxPopulation(int maxPopulation) {
        this.maxPopulation = maxPopulation;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setMaxWeight(float maxWeight) {
        if (maxWeight > 0f) {
            this.maxWeight = maxWeight;
        } else {
            throw new IllegalArgumentException("Max weight must be greater than zero");
        }
    }

    public synchronized void handleRotting() {
        this.setTicksToRotting(this.getTicksToRotting() - 1);

        if (this.getTicksToRotting() == 0) {
            IslandCell currentIslandCell = Island.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
            synchronized (currentIslandCell) {
//                System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") has rotten away!");
                currentIslandCell.removeCreature(this);
            }
        }
    }

    public int getTicksToRotting() {
        return ticksToRotting;
    }

    public void setTicksToRotting(int ticksToRotting) {
        if (ticksToRotting < 0) {
            this.ticksToRotting = 0;
        }
        this.ticksToRotting = ticksToRotting;
    }

    public int getWasKilledOnTick() {
        return wasKilledOnTick;
    }

    public void setWasKilledOnTick(int wasKilledOnTick) {
        this.wasKilledOnTick = wasKilledOnTick;
    }
}
