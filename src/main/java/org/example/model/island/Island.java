package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.herbivore.Boar;
import org.example.model.creature.animal.herbivore.Bull;
import org.example.model.creature.animal.herbivore.Caterpillar;
import org.example.model.creature.animal.herbivore.Deer;
import org.example.model.creature.animal.herbivore.Duck;
import org.example.model.creature.animal.herbivore.Goat;
import org.example.model.creature.animal.herbivore.Herbivore;
import org.example.model.creature.animal.herbivore.Horse;
import org.example.model.creature.animal.herbivore.Mouse;
import org.example.model.creature.animal.herbivore.Rabbit;
import org.example.model.creature.animal.herbivore.Sheep;
import org.example.model.creature.animal.predator.Bear;
import org.example.model.creature.animal.predator.Eagle;
import org.example.model.creature.animal.predator.Fox;
import org.example.model.creature.animal.predator.Predator;
import org.example.model.creature.animal.predator.Snake;
import org.example.model.creature.animal.predator.Wolf;
import org.example.model.creature.plant.Plant;
import org.example.utils.Config;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Island {
    private static ArrayList<IslandCell> islandCells;
    private static int length;
    private static int width;
    private int tick;
    private static final Config config = Config.getInstance();

    public Island(int sizeX, int sizeY) {
        length = sizeX;
        width = sizeY;
        islandCells = new ArrayList<>();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                islandCells.add(new IslandCell(i, j));
            }
        }

        tick = 1;
        printCreatedCreatures();
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
                System.out.println("*** Start of the Tick: " + tick + " ***");
                ArrayList<Thread> threads = new ArrayList<>();

                for (IslandCell islandCell : islandCells) {
                    Thread thread = new Thread(islandCell);
                    threads.add(thread);
                    thread.start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }

                printStatistic();
                System.out.println("*** End of the Tick: " + tick + " ***");
                tick++;

//                if (Tick % 10 == 0) {
//                    System.out.println("Tick: " + Tick);
//                }
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

    private void printCreatedCreatures() {
        Map<Class<? extends Creature>, Long> creatures = islandCells.stream().flatMap(
                ic -> ic.getCreatures()
                        .stream()
        ).collect(Collectors.groupingBy(Creature::getClass, Collectors.counting()));

        System.out.println("On the island [" + getLength() + "," + getWidth() + "] created: ");
        System.out.print(config.get("wolf.icon") + ": " + creatures.getOrDefault(Wolf.class, 0L) + ", ");
        System.out.print(config.get("snake.icon") + ": " + creatures.getOrDefault(Snake.class, 0L) + ", ");
        System.out.print(config.get("fox.icon") + ": " + creatures.getOrDefault(Fox.class, 0L) + ", ");
        System.out.print(config.get("bear.icon") + ": " + creatures.getOrDefault(Bear.class, 0L) + ", ");
        System.out.print(config.get("eagle.icon") + ": " + creatures.getOrDefault(Eagle.class, 0L) + ", ");
        System.out.print(config.get("horse.icon") + ": " + creatures.getOrDefault(Horse.class, 0L) + ", ");
        System.out.print(config.get("deer.icon") + ": " + creatures.getOrDefault(Deer.class, 0L) + ", ");
        System.out.print(config.get("rabbit.icon") + ": " + creatures.getOrDefault(Rabbit.class, 0L) + ", ");
        System.out.print(config.get("mouse.icon") + ": " + creatures.getOrDefault(Mouse.class, 0L) + ", ");
        System.out.print(config.get("goat.icon") + ": " + creatures.getOrDefault(Goat.class, 0L) + ", ");
        System.out.print(config.get("sheep.icon") + ": " + creatures.getOrDefault(Sheep.class, 0L) + ", ");
        System.out.print(config.get("boar.icon") + ": " + creatures.getOrDefault(Boar.class, 0L) + ", ");
        System.out.print(config.get("bull.icon") + ": " + creatures.getOrDefault(Bull.class, 0L) + ", ");
        System.out.print(config.get("duck.icon") + ": " + creatures.getOrDefault(Duck.class, 0L) + ", ");
        System.out.print(config.get("caterpillar.icon") + ": " + creatures.getOrDefault(Caterpillar.class, 0L) + ", ");
        System.out.println(config.get("plant.icon") + ": " + creatures.getOrDefault(Plant.class, 0L));
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

    public static int getWidth() {
        return width;
    }

    public static int getLength() {
        return length;
    }
}
