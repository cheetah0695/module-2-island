package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.creature.animal.CreatureFactory;
import org.example.model.creature.animal.herbivore.Boar;
import org.example.model.creature.animal.herbivore.Bull;
import org.example.model.creature.animal.herbivore.Caterpillar;
import org.example.model.creature.animal.herbivore.Deer;
import org.example.model.creature.animal.herbivore.Duck;
import org.example.model.creature.animal.herbivore.Goat;
import org.example.model.creature.animal.herbivore.Horse;
import org.example.model.creature.animal.herbivore.Mouse;
import org.example.model.creature.animal.herbivore.Rabbit;
import org.example.model.creature.animal.herbivore.Sheep;
import org.example.model.creature.animal.predator.Bear;
import org.example.model.creature.animal.predator.Eagle;
import org.example.model.creature.animal.predator.Fox;
import org.example.model.creature.animal.predator.Snake;
import org.example.model.creature.animal.predator.Wolf;
import org.example.model.creature.plant.Plant;
import org.example.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class IslandCell {
    private final int x;
    private final int y;
    private final int id;
    private volatile CopyOnWriteArrayList<Creature> creatures;
    private final Config config = Config.getInstance();

    public IslandCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = x * Island.getWidth() + y;

        initializeCreatures();
    }

    public synchronized ArrayList<Creature> getCreatures() {
        return new ArrayList<>(creatures);
    }

    public synchronized void addCreature(Creature creature) {
        if (creatures != null) {
            creatures.add(creature);
        }
    }

    public synchronized void removeCreature(Creature c) {
        synchronized (creatures) {
            creatures.remove(c);
        }
    }

    public synchronized boolean isPopulationFull(Creature creature) {
        return getCreatures().stream()
                .filter(c -> c.getClass() == creature.getClass())
                .count() == creature.getMaxPopulation();
    }

    public ArrayList<IslandCell> findCellsToMigrate(Animal animal) {
        ArrayList<IslandCell> cellsToMigrate = new ArrayList<>();
        int maxRange = animal.getMaxMovementRange();

        for (int offsetX = -maxRange; offsetX <= animal.getMaxMovementRange(); offsetX++) {
            for (int offsetY = -maxRange; offsetY <= animal.getMaxMovementRange(); offsetY++) {
                IslandCell calculatedCell = Island.getIslandCell(
                        animal.getCurrentIslandCellX() + offsetX, animal.getCurrentIslandCellY() + offsetY
                );

                if (calculatedCell != null && !calculatedCell.isPopulationFull(animal)) {
                    cellsToMigrate.add(calculatedCell);
                }
            }
        }

        return cellsToMigrate;
    }

    public Creature getPrey(Animal animal) {
        List<Creature> possibleCreaturesToEat = this.getCreatures().stream()
                .filter(c -> animal.getPossibleFoodTable().containsKey(c.getClass()) &&
                        c.getCurrentWeight() != 0).collect(Collectors.toList());

        if (possibleCreaturesToEat.isEmpty()) {
            return null;
        }

        int randomCreatureIndex = ThreadLocalRandom.current().nextInt(0, possibleCreaturesToEat.size());
        return possibleCreaturesToEat.get(randomCreatureIndex);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    private void logAliveAnimals(Class<? extends Creature> animalClass) {
        logAliveAnimals(animalClass, false);
    }

    private void logAliveAnimals(Class<? extends Creature> animalClass, boolean printIds) {
        List<Creature> animals = this.getCreatures()
                .stream()
                .filter(c -> animalClass.isAssignableFrom(c.getClass()) && c.isAlive())
                .collect(Collectors.toList());

        System.out.println("Alive " + animalClass.getSimpleName() + "s in the island cell [" + this.getX() +
                "," + this.getY() + "] left: " + animals.size());

        if (!animals.isEmpty()) {
            System.out.print(("Alive " + animalClass.getSimpleName() + "s left with ids: "));

            for (int i = 0; i < animals.size(); i++) {
                if (i != animals.size() - 1) {
                    System.out.print(animals.get(i).getId() + ",");
                } else {
                    System.out.print(animals.get(i).getId());
                }
            }
            System.out.println();
        }
    }

    private void initializeCreatures() {
        creatures = new CopyOnWriteArrayList<>();

        createCreature(creatures, Wolf::new, "Wolf", ThreadLocalRandom.current().nextInt(
                0, config.getInt("wolf" + ".max-population") + 1)
        );
        createCreature(creatures, Snake::new, "Snake", ThreadLocalRandom.current().nextInt(
                0, config.getInt("snake" + ".max-population") + 1)
        );
        createCreature(creatures, Fox::new, "Fox", ThreadLocalRandom.current().nextInt(
                0, config.getInt("fox" + ".max-population") + 1)
        );
        createCreature(creatures, Bear::new, "Bear", ThreadLocalRandom.current().nextInt(
                0, config.getInt("bear" + ".max-population") + 1)
        );
        createCreature(creatures, Eagle::new, "Eagle", ThreadLocalRandom.current().nextInt(
                0, config.getInt("eagle" + ".max-population") + 1)
        );
        createCreature(creatures, Horse::new, "Horse", ThreadLocalRandom.current().nextInt(
                0, config.getInt("horse" + ".max-population") + 1)
        );
        createCreature(creatures, Deer::new, "Deer", ThreadLocalRandom.current().nextInt(
                0, config.getInt("deer" + ".max-population") + 1)
        );
        createCreature(creatures, Rabbit::new, "Rabbit", ThreadLocalRandom.current().nextInt(
                0, config.getInt("rabbit" + ".max-population") + 1)
        );
        createCreature(creatures, Mouse::new, "Mouse", ThreadLocalRandom.current().nextInt(
                0, config.getInt("mouse" + ".max-population") + 1)
        );
        createCreature(creatures, Goat::new, "Goat", ThreadLocalRandom.current().nextInt(
                0, config.getInt("goat" + ".max-population") + 1)
        );
        createCreature(creatures, Sheep::new, "Sheep", ThreadLocalRandom.current().nextInt(
                0, config.getInt("sheep" + ".max-population") + 1)
        );
        createCreature(creatures, Boar::new, "Boar", ThreadLocalRandom.current().nextInt(
                0, config.getInt("boar" + ".max-population") + 1)
        );
        createCreature(creatures, Bull::new, "Bull", ThreadLocalRandom.current().nextInt(
                0, config.getInt("bull" + ".max-population") + 1)
        );
        createCreature(creatures, Duck::new, "Duck", ThreadLocalRandom.current().nextInt(
                0, config.getInt("duck" + ".max-population") + 1)
        );
        createCreature(creatures, Caterpillar::new, "Caterpillar", ThreadLocalRandom.current().nextInt(
                0, config.getInt("caterpillar" + ".max-population") + 1)
        );
        createCreature(creatures, Plant::new, "Plant", ThreadLocalRandom.current().nextInt(
                0, config.getInt("plant" + ".max-population") + 1)
        );
    }

    private void createCreature(CopyOnWriteArrayList<Creature> creatures,
                                CreatureFactory creatureFactory,
                                String name,
                                int creatureCount
    ) {
        for (int i = 0; i < creatureCount; i++) {
            try {
                Creature creature = creatureFactory.createCreature(getX(), getY());
                creatures.add(creature);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create creature: ", e);
            }
        }
//        System.out.println("Created " + creatureCount + " " + name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object cell) {
        if (cell == null)
            return false;

        if (this == cell)
            return true;

        if (!(cell instanceof IslandCell)) {
            return false;
        } else {
            IslandCell islandCell = (IslandCell) cell;
            return this.getX() == islandCell.getX() && this.getY() == islandCell.getY();
        }
    }
}
