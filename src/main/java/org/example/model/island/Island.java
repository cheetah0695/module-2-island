package org.example.model.island;

import org.example.model.creature.Creature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Island {
    private ArrayList<IslandCell> islandCells;

    public Island(int sizeX, int sizeY) {
        islandCells = new ArrayList<>();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                islandCells.add(new IslandCell(i, j));
            }
        }
    }

    public ArrayList<IslandCell> getIslandCells() {
        return islandCells;
    }

    public void runSimulation() {
        for (IslandCell islandCell : islandCells) {
            List<Creature> newborns = new ArrayList<>();
            System.out.println("Start of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
            for (Creature creature : islandCell.getCreatures()) {
                creature.reproduce(islandCell, newborns);
            }

            newborns.addAll(newborns);
            System.out.println("End of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
        }

        for (IslandCell islandCell : islandCells) {
            System.out.println();
        }
    }
}
