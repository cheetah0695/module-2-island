package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.herbivore.Herbivore;
import org.example.model.creature.animal.predator.Predator;
import org.example.model.creature.plant.Plant;

import java.util.ArrayList;

public class Island {
    private static ArrayList<IslandCell> islandCells;
    private static int maxWidth;
    private int Tick;

    public Island(int sizeX, int sizeY) {
        maxWidth = sizeY;
        islandCells = new ArrayList<>();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                islandCells.add(new IslandCell(i, j));
            }
        }

        Tick = 1;
    }

    public static ArrayList<IslandCell> getIslandCells() {
        return islandCells;
    }

    public static IslandCell getIslandCell(int x, int y) {
        return getIslandCells().stream().filter(ic -> ic.getX() == x && ic.getY() == y).findFirst()
                .orElse(null);
    }

    public void runSimulation() {
        runTicks();
    }

    private void runTicks() {
        try {
            while (hasAlivePredators()) {
                ArrayList<Thread> threads = new ArrayList<>();

                for (IslandCell islandCell : islandCells) {
                    Thread thread = new Thread(islandCell);
                    threads.add(thread);
                    thread.start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }

                System.out.println("*** End of the Tick: " + Tick + " ***");
                printStatistic();
                Tick++;

                if (Tick % 10 == 0) {
                    System.out.println("Tick: " + Tick);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private boolean hasAlivePredators() {
        for (IslandCell islandCell : Island.getIslandCells()) {
            ArrayList<Creature> snapshot;

            synchronized (islandCell) {
                snapshot = new ArrayList<>(islandCell.getCreatures());
            }

            for (Creature creature : snapshot) {
                if (creature.isAlive() && creature instanceof Predator) {
                    return true;
                }
            }
        }
        return false;
    }

    private void printStatistic() {
        int plantsCount = 0;
        int herbivoresCount = 0;
        int predatorsCount = 0;

        for (IslandCell islandCell : islandCells) {
            for (Creature creature : islandCell.getCreatures()) {
                if (creature.isAlive()) {
                    if (creature instanceof Plant) {
                        plantsCount++;
                    }
                    if (creature instanceof Herbivore) {
                        herbivoresCount++;
                    }
                    if (creature instanceof Predator) {
                        predatorsCount++;
                    }
                }
            }
        }
        System.out.println("Alive plants in all cells: " + plantsCount);
        System.out.println("Alive herbivores in all cells: " + herbivoresCount);
        System.out.println("Alive predators in all cells: " + predatorsCount);
    }

    public static int getMaxWidth() {
        return maxWidth;
    }
}
