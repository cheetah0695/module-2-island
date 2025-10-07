package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.herbivore.Goat;
import org.example.model.creature.animal.herbivore.Horse;
import org.example.model.creature.animal.predator.Bear;
import org.example.model.creature.animal.predator.Fox;
import org.example.model.creature.plant.Plant;
import org.example.utils.IdGeneratorUtil;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class IslandCell {
    private int x;
    private int y;
    private ArrayList<Creature> creatures;

    public IslandCell(int x, int y) {
        this.x = x;
        this.y = y;
        initializeCreatures();
        System.out.println("LAST ID: " + IdGeneratorUtil.getId());
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
        createCreature(creatures, Bear.class, ThreadLocalRandom.current().nextInt(0, 6));
//        createCreature(creatures, Eagle.class, ThreadLocalRandom.current().nextInt(0, 21));
        createCreature(creatures, Horse.class, ThreadLocalRandom.current().nextInt(0, 21));
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
        System.out.println("END: Creation of creatures on the island cell: [" + x + "," + y + "]");
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
