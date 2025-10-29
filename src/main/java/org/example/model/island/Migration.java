package org.example.model.island;

public class Migration {
    private IslandCell currentIslandCell;
    private IslandCell newIslandCell;

    public Migration(IslandCell currentIslandCell, IslandCell newIslandCell) {
        this.currentIslandCell = currentIslandCell;
        this.newIslandCell = newIslandCell;
    }

    public IslandCell getCurrentIslandCell() {
        return currentIslandCell;
    }

    public IslandCell getNewIslandCell() {
        return newIslandCell;
    }
}
