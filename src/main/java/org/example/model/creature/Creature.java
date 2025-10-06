package org.example.model.creature;

import org.example.model.island.IslandCell;

import java.util.List;

public abstract class Creature {
    private long id;
    private static long maxId;
    private float weight;
    private int currentIslandCellX;
    private int currentIslandCellY;
    private int maxPopulation;

    public abstract void reproduce(IslandCell islandCell, List<Creature> newborns);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static long getMaxId() {
        return maxId;
    }

    public static void setMaxId(long maxId) {
        Creature.maxId = maxId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
}
