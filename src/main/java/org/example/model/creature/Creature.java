package org.example.model.creature;

import org.example.model.island.IslandCell;
import org.example.utils.IdGeneratorUtil;

public abstract class Creature {
    private long id;
    private float maxWeight;
    private float currentWeight;
    private int currentIslandCellX;
    private int currentIslandCellY;
    private int maxPopulation;
    private boolean isAlive;

    protected Creature(int currentIslandCellX, int currentIslandCellY) {
        this.id = IdGeneratorUtil.nextId();
        isAlive = true;
        setCurrentIslandCellX(currentIslandCellX);
        setCurrentIslandCellY(currentIslandCellY);
    }

    public abstract void eat();

    public abstract void reproduce(IslandCell islandCell);

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

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        if (maxWeight > 0f) {
            this.maxWeight = maxWeight;
        } else {
            throw  new IllegalArgumentException("Max weight must be greater than zero");
        }
    }
}
