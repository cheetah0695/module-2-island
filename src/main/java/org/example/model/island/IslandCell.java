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
import org.example.model.creature.animal.predator.Predator;
import org.example.model.creature.animal.predator.Snake;
import org.example.model.creature.animal.predator.Wolf;
import org.example.model.creature.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class IslandCell implements Runnable {
    private final int x;
    private final int y;
    private final int id;
    private ArrayList<Creature> creatures;

    public IslandCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = x * Island.getWidth() + y;
        initializeCreatures();
//        System.out.println("CREATED Island Cell [" + x + "," + y + "] with: " + creatures.size() +
//                " creatures. Predators: " + creatures.stream().filter(c -> c instanceof Predator).count() +
//                ". Herbivores: " + creatures.stream().filter(c -> c instanceof Herbivore).count() +
//                ". Plants: " + creatures.stream().filter(c -> c instanceof Plant).count()
//        );
//        System.out.println("***********************************");
    }

    @Override
    public void run() {
        runEat();
        List<Animal> islandAnimals = getAliveAnimals();
        runReproduce();
        runMove();
        increaseHungerOfAnimals(islandAnimals);
    }

    private void runMove() {

        ArrayList<Migration> migrations = getMigrations();
        applyMigration(migrations);

        resetMovedThisTick();
    }

    private ArrayList<Migration> getMigrations() {
        ArrayList<Migration> migrations = new ArrayList<>();

        for (Animal animal : this.getAliveAnimals()) {
            if (!animal.isMovedThisTick()) {
                Migration migration = animal.createMigration();

                if (migration != null) {
                    migrations.add(migration);
                }
            }
        }

        return migrations;
    }

    private void applyMigration(ArrayList<Migration> migrations) {
        if (!migrations.isEmpty()) {
            for (Migration migration : migrations) {
                IslandCell currentIslandCell = migration.getCurrentIslandCell();
                IslandCell newIslandCell = migration.getNewIslandCell();

                if (newIslandCell != currentIslandCell) {
                    IslandCell firstLock = currentIslandCell.getId() < newIslandCell.getId() ?
                            currentIslandCell : newIslandCell;
                    IslandCell secondLock = currentIslandCell.getId() < newIslandCell.getId() ?
                            newIslandCell : currentIslandCell;

                    synchronized (firstLock) {
                        synchronized (secondLock) {
                            Animal migratingAnimal = migration.getAnimal();
                            migratingAnimal.setCurrentIslandCellX(newIslandCell.getX());
                            migratingAnimal.setCurrentIslandCellY(newIslandCell.getY());
                            migratingAnimal.setRemainingHunger((float) (migratingAnimal.getRemainingHunger() * 1.5));
                            newIslandCell.addCreature(migration.getAnimal());
                            currentIslandCell.removeCreature(migratingAnimal);
                        }
                    }
                }
            }
        }
    }

    public synchronized void removeCreature(Creature c) {
        creatures.remove(c);
    }

    private void resetMovedThisTick() {
        for (Animal animal : this.getAliveAnimals()) {
            animal.setMovedThisTick(false);
        }
    }

    private void increaseHungerOfAnimals(List<Animal> islandAnimals) {
        islandAnimals.forEach(animal -> animal.setRemainingHunger((animal.getRequiredFood())));
    }

    private void runReproduce() {
//            System.out.println("Start of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
        ArrayList<Creature> initialList = new ArrayList<>(this.getCreatures());

        for (Creature creature : initialList) {
            creature.reproduce(this);
        }
//        System.out.println("End of reproducing for the island cell: [" + this.getX() + "," + this.getY() + "]");
    }

    private void runEat() {
        ArrayList<Creature> creaturesToRemove = new ArrayList<>();

        for (Animal animal : this.getAliveAnimals()) {
            if (animal.getRemainingHunger() == 0) {
//                System.out.println(animal.getClass().getSimpleName() + " (id: " + animal.getId() +
//                        ") refuses to hunt because it doesn't need food right now"
//                );
                continue;
            }

            Creature prey = this.getPrey(animal);

            if (prey != null && animal.isAlive()) {
                if (animal instanceof Predator || !(prey instanceof Plant)) {
                    animal.tryToEat(prey);
                    addCreatureToRemoveList(prey, creaturesToRemove);
                } else {
                    while (animal.getRemainingHunger() > 0 && prey instanceof Plant) {
                        if (prey.getCurrentWeight() == 0) {
                            prey = this.getPrey(animal);
                        }

                        if (prey != null) {
                            animal.tryToEat(prey);
                            addCreatureToRemoveList(prey, creaturesToRemove);
                        }
                    }
                }
            }

            if ((animal.getRemainingHunger() > animal.getRequiredFood() / 2) && animal.isAlive()) {
                animal.decrementTicksToStarvation();

                if (animal.getTicksToStarvingLeft() == 0) {
                    creaturesToRemove.add(animal);
                }
            }
        }

        this.removeAllCreatures(creaturesToRemove);

//        System.out.println("Statistic for the cell: [" + this.getX() + "," + this.getY() + "]");
//        logAliveAnimals(Plant.class);
//        logAliveAnimals(Herbivore.class);
//        logAliveAnimals(Predator.class);
//        System.out.println("End of statistic for the cell: [" + this.getX() + "," + this.getY() + "]");
    }

    private synchronized void removeAllCreatures(ArrayList<Creature> creaturesToRemove) {
        creatures.removeAll(creaturesToRemove);
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

    private void addCreatureToRemoveList(Creature prey, ArrayList<Creature> creaturesToRemove) {
        if (prey != null && prey.getCurrentWeight() == 0) {
            creaturesToRemove.add(prey);

//            System.out.println("Prey: " + prey.getClass().getSimpleName() + " (id: " +
//                    prey.getId() + ") was removed!");
        }
    }

    public synchronized List<Animal> getAliveAnimals() {
        return this.getCreatures().stream().filter(c -> c instanceof Animal && c.isAlive())
                .map(c -> (Animal) c).collect(Collectors.toList());
    }

    public synchronized void addCreature(Creature creature) {
        creatures.add(creature);
    }

    private void createCreature(ArrayList<Creature> creatures,
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

    private void initializeCreatures() {
        creatures = new ArrayList<>();
//        System.out.println("START: Creation of creatures on the island cell: [" + x + "," + y + "]");
        createCreature(creatures, Wolf::new, "Wolf", ThreadLocalRandom.current().nextInt(0, 31));
        createCreature(creatures, Snake::new,"Snake", ThreadLocalRandom.current().nextInt(0, 31));
        createCreature(creatures, Fox::new, "Fox", ThreadLocalRandom.current().nextInt(0, 31));
        createCreature(creatures, Bear::new,"Bear", ThreadLocalRandom.current().nextInt(0, 6));
        createCreature(creatures, Eagle::new,"Eagle", ThreadLocalRandom.current().nextInt(0, 21));
        createCreature(creatures, Horse::new,"Horse", ThreadLocalRandom.current().nextInt(0, 21));
        createCreature(creatures, Deer::new,"Deer", ThreadLocalRandom.current().nextInt(0, 21));
        createCreature(creatures, Rabbit::new,"Rabbit", ThreadLocalRandom.current().nextInt(0, 151));
        createCreature(creatures, Mouse::new,"Mouse", ThreadLocalRandom.current().nextInt(0, 501));
        createCreature(creatures, Goat::new,"Goat", ThreadLocalRandom.current().nextInt(0, 141));
        createCreature(creatures, Sheep::new,"Sheep", ThreadLocalRandom.current().nextInt(0, 141));
        createCreature(creatures, Boar::new,"Boar", ThreadLocalRandom.current().nextInt(0, 51));
        createCreature(creatures, Bull::new,"Bull", ThreadLocalRandom.current().nextInt(0, 11));
        createCreature(creatures, Duck::new,"Duck", ThreadLocalRandom.current().nextInt(0, 201));
        createCreature(creatures, Caterpillar::new,"Caterpillar", ThreadLocalRandom.current().nextInt(0, 1001));
        createCreature(creatures, Plant::new,"Plant", ThreadLocalRandom.current().nextInt(0, 201));

//        createCreature(creatures, (x, y) -> new Plant(x, y),"Plant", ThreadLocalRandom.current().nextInt(100, 101));
//        createCreature(creatures, (x, y) -> new Wolf(x, y), "Wolf", ThreadLocalRandom.current().nextInt(1, 2));
//        createCreature(creatures, (x, y) -> new Goat(x, y),"Goat", ThreadLocalRandom.current().nextInt(5, 6));
    }

    public synchronized ArrayList<Creature> getCreatures() {
        return new ArrayList<>(creatures);
    }

    public synchronized boolean isPopulationFull(Creature creature) {
        return getCreatures().stream().filter(c -> c.getClass() == creature.getClass()).count() == creature.getMaxPopulation();
    }

    public ArrayList<IslandCell> findCellsToMigrate(Animal animal) {
        ArrayList<IslandCell> cellsToMigrate = new ArrayList<>();
        int maxRange = animal.getMaxMovementRange();

        for (int offsetX = -maxRange; offsetX <= animal.getMaxMovementRange(); offsetX++) {
            for (int offsetY = -maxRange; offsetY <= animal.getMaxMovementRange(); offsetY++) {
                IslandCell calculatedCell = Island.getIslandCell(animal.getCurrentIslandCellX() + offsetX, animal.getCurrentIslandCellY() + offsetY);

                if (calculatedCell != null && !calculatedCell.isPopulationFull(animal)) {
                    cellsToMigrate.add(calculatedCell);
                }
            }
        }

        return cellsToMigrate;
    }

    public Creature getPrey(Animal animal) {
        List<Creature> snapshot;

        // создаём копию, чтобы избежать изменений во время итерации
        synchronized (this) {
            snapshot = new ArrayList<>(this.getCreatures());
        }

        List<Creature> possibleCreaturesToEat = snapshot.stream()
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
