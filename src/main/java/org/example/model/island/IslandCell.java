package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class IslandCell {
    private int x;
    private int y;
    private ArrayList<Creature> creatures;

    public IslandCell(int x, int y) {
        this.x = x;
        this.y = y;
        initializeCreatures();
        System.out.println("CREATED Island Cell [" + x + "," + y + "] with: " + creatures.size() +
                " creatures. Predators: " + creatures.stream().filter(c -> c instanceof Predator).count() +
                ". Herbivores: " + creatures.stream().filter(c -> c instanceof Herbivore).count() +
                ". Plants: " + creatures.stream().filter(c -> c instanceof Plant).count()
        );
        System.out.println("***********************************");
    }

    public void addCreature(Creature creature) {
        creatures.add(creature);
    }

    private void createCreature(ArrayList<Creature> creatures, Class<? extends Creature> creatureClass, int creatureCount) {
        for (int i = 0; i < creatureCount; i++) {
            try {
                Creature creature = creatureClass.getDeclaredConstructor(int.class, int.class).newInstance(x, y);
                creatures.add(creature);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create: " + creatureClass.getSimpleName(), e);
            }
        }
        System.out.println("Created " + creatureCount + " " + creatureClass.getSimpleName());
    }

    private void initializeCreatures() {
        creatures = new ArrayList<>();
        System.out.println("START: Creation of creatures on the island cell: [" + x + "," + y + "]");
//        createCreature(creatures, Wolf.class, ThreadLocalRandom.current().nextInt(0, 31));
//        createCreature(creatures, Snake.class, ThreadLocalRandom.current().nextInt(0, 31));
//        createCreature(creatures, Fox.class, ThreadLocalRandom.current().nextInt(0, 31));
//        createCreature(creatures, Bear.class, ThreadLocalRandom.current().nextInt(0, 6));
//        createCreature(creatures, Eagle.class, ThreadLocalRandom.current().nextInt(0, 21));
//        createCreature(creatures, Horse.class, ThreadLocalRandom.current().nextInt(0, 21));
//        createCreature(creatures, Deer.class, ThreadLocalRandom.current().nextInt(0, 21));
//        createCreature(creatures, Rabbit.class, ThreadLocalRandom.current().nextInt(0, 151));
//        createCreature(creatures, Mouse.class, ThreadLocalRandom.current().nextInt(0, 501));
//        createCreature(creatures, Goat.class, ThreadLocalRandom.current().nextInt(0, 141));
//        createCreature(creatures, Sheep.class, ThreadLocalRandom.current().nextInt(0, 141));
//        createCreature(creatures, Boar.class, ThreadLocalRandom.current().nextInt(0, 51));
//        createCreature(creatures, Bull.class, ThreadLocalRandom.current().nextInt(0, 11));
//        createCreature(creatures, Duck.class, ThreadLocalRandom.current().nextInt(0, 201));
//        createCreature(creatures, Caterpillar.class, ThreadLocalRandom.current().nextInt(0, 1001));
//        createCreature(creatures, Plant.class, ThreadLocalRandom.current().nextInt(0, 201));





//        createCreature(creatures, Boar.class, ThreadLocalRandom.current().nextInt(0, 10));
//        createCreature(creatures, Plant.class, ThreadLocalRandom.current().nextInt(0, 210));
//        createCreature(creatures, Mouse.class, ThreadLocalRandom.current().nextInt(0, 3));
//        createCreature(creatures, Caterpillar.class, ThreadLocalRandom.current().nextInt(0, 5));

//        createCreature(creatures, Wolf.class, ThreadLocalRandom.current().nextInt(0, 2));
//        createCreature(creatures, Rabbit.class, ThreadLocalRandom.current().nextInt(0, 5));
//        createCreature(creatures, Plant.class, ThreadLocalRandom.current().nextInt(0, 10));

//        createCreature(creatures, Wolf.class, ThreadLocalRandom.current().nextInt(0, 4));
//        createCreature(creatures, Goat.class, ThreadLocalRandom.current().nextInt(0, 10));

        createCreature(creatures, Goat.class, ThreadLocalRandom.current().nextInt(0, 2));
        createCreature(creatures, Plant.class, ThreadLocalRandom.current().nextInt(0, 6));
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public boolean checkPopulationFull(Creature creature) {
        return getCreatures().stream().filter(c -> c.getClass() == creature.getClass()).count() == creature.getMaxPopulation();
    }

    public ArrayList<IslandCell> findCellsToMigrate(Creature creature, int range) {
        ArrayList<IslandCell> cellsToMigrate = new ArrayList<>();
//
        cellsToMigrate.add(Island.getIslandCell(getX() + range, getY()));
        cellsToMigrate.add(Island.getIslandCell(getX() + range, getY() + range));
        cellsToMigrate.add(Island.getIslandCell(getX() + range, getY() - range));
        cellsToMigrate.add(Island.getIslandCell(getX() - range, getY()));
        cellsToMigrate.add(Island.getIslandCell(getX() - range, getY() + range));
        cellsToMigrate.add(Island.getIslandCell(getX() - range, getY() - range));
        cellsToMigrate.add(Island.getIslandCell(getX(), getY() + range));
        cellsToMigrate.add(Island.getIslandCell(getX(), getY() - range));

        return cellsToMigrate.stream().filter(ctm -> ctm != null && !ctm.checkPopulationFull(creature))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Creature getPrey(Animal animal) {
        List<Creature> possibleCreaturesToEat = this.getCreatures().stream()
                .filter(c -> animal.getPossibleFoodTable().containsKey(c.getClass()) && c.getCurrentWeight() != 0).collect(Collectors.toList());

        if (possibleCreaturesToEat.size() == 0) {
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
}
