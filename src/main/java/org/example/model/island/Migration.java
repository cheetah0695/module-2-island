package org.example.model.island;

import org.example.model.creature.animal.Animal;

public class Migration {
    private Animal animal;
    private IslandCell currentIslandCell;
    private IslandCell newIslandCell;

    public Migration(Animal animal, IslandCell currentIslandCell, IslandCell newIslandCell) {
        this.animal = animal;
        this.currentIslandCell = currentIslandCell;
        this.newIslandCell = newIslandCell;
    }

    public Animal getAnimal() {
        return animal;
    }

    public IslandCell getCurrentIslandCell() {
        return currentIslandCell;
    }

    public IslandCell getNewIslandCell() {
        return newIslandCell;
    }
}
