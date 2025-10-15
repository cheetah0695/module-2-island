package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.creature.animal.herbivore.Herbivore;
import org.example.model.creature.animal.predator.Predator;
import org.example.model.creature.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Island {
    private static ArrayList<IslandCell> islandCells;
    private int Tick;

    public Island(int sizeX, int sizeY) {
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
        runTick();
    }

    private void runTick() {
        List<Creature> islandAnimals = getAliveAnimals();

        while (islandAnimals.size() > 0 && !islandAnimals.stream().allMatch(c -> c instanceof Plant)) {
            runEat();
            islandAnimals = getAliveAnimals();
//            runReproduce();
//            runMove();
            System.out.println("*** End of the Tick: " + Tick + " ***");
            increaseHunger(islandAnimals);
            Tick++;
        }
    }

    private List<Creature> getAliveAnimals() {
        return islandCells.stream()
                .map(ic -> ic.getCreatures())
                .flatMap(List::stream)
                .filter(c -> c instanceof Animal && c.isAlive())
                .collect(Collectors.toList());
    }

    private void increaseHunger(List<Creature> islandAnimals) {
        islandAnimals.forEach(creature -> {
            ((Animal) creature).setRemainingHunger(((Animal) creature).getRequiredFood());
        });
    }

    private void runEat() {
        for (IslandCell islandCell : islandCells) {
            ArrayList<Creature> creaturesToRemove = new ArrayList<>();

            for (Creature creature : islandCell.getCreatures()) {
                if (creature instanceof Animal) {
                    Animal animal = (Animal) creature;

                    if (!animal.isAlive()) {
                        System.out.println(animal.getClass().getSimpleName() + " (id: " + creature.getId() +
                                ") can not hunt because it dead!"
                        );
                        continue;
                    }

                    if (animal.getRemainingHunger() == 0) {
                        System.out.println(animal.getClass().getSimpleName() + " (id: " + creature.getId() +
                                ") refuses to hunt because it doesn't need food right now"
                        );
                        continue;
                    }

                    Creature prey = islandCell.getPrey(animal);

                    if (prey != null) {
                        if (animal instanceof Predator) {
                            animal.tryToEat(prey);
                            addCreatureToRemoveList(prey, creaturesToRemove);
                        } else {
                            while (animal.getRemainingHunger() > 0 && prey != null && prey instanceof Plant) {
                                if (prey.getCurrentWeight() == 0) {
                                    prey = islandCell.getPrey(animal);
                                }

                                if (prey != null) {
                                    animal.tryToEat(prey);
                                    addCreatureToRemoveList(prey, creaturesToRemove);
                                }
                            }
                        }
                    } else {
                        if (animal.isAlive()) {
                            System.out.println(animal.getClass().getSimpleName() + " (id: " + animal.getId() +
                                    ") not enough food in [" + islandCell.getX() + "," + islandCell.getY() +
                                    "]. Remaining hunger: " + animal.getRemainingHunger() + ". Days to starvation: "
                                    + animal.getTicksToStarvingLeft());
                        }
                    }

                    if ((animal.getRemainingHunger() > animal.getRequiredFood() / 2) && animal.isAlive()) {
                        animal.decrementTicksToStarvation();

                        if (animal.getTicksToStarvingLeft() == 0) {
                            creaturesToRemove.add(animal);
                        }
                    }
                }
            }

            islandCell.getCreatures().removeAll(creaturesToRemove);

            System.out.println("Statistic for the cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
            logAliveAnimals(Plant.class, islandCell);
            logAliveAnimals(Herbivore.class, islandCell);
            logAliveAnimals(Predator.class, islandCell);
            System.out.println("End of statistic for the cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
        }
    }

    private void addCreatureToRemoveList(Creature prey, ArrayList<Creature> creaturesToRemove) {
        if (prey != null && prey.getCurrentWeight() == 0) {
            creaturesToRemove.add(prey);

            System.out.println("Prey: " + prey.getClass().getSimpleName() + " (id: " +
                    prey.getId() + ") was removed!");
        }
    }

    private void logAliveAnimals(Class<? extends Creature> animalClass, IslandCell islandCell) {
        logAliveAnimals(animalClass, islandCell, false);
    }

    private void logAliveAnimals(Class<? extends Creature> animalClass, IslandCell islandCell, boolean printIds) {
        List<Creature> animals = islandCell.getCreatures()
                .stream()
                .filter(c -> animalClass.isAssignableFrom(c.getClass()) && c.isAlive())
                .collect(Collectors.toList());

        System.out.println("Alive " + animalClass.getSimpleName() + "s in the island cell [" + islandCell.getX() +
                "," + islandCell.getY() + "] left: " + animals.size());

        if (animals.size() > 0 && printIds) {
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

    private void runReproduce() {
        for (IslandCell islandCell : islandCells) {
            System.out.println("Start of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");

            ArrayList<Creature> initialList = new ArrayList<>(islandCell.getCreatures());

            for (Creature creature : initialList) {
                creature.reproduce(islandCell);
            }

            System.out.println("End of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
        }
    }

    private void runMove() {
        for (IslandCell islandCell : islandCells) {
            ArrayList<Migration> migrations = getMigrations(islandCell);

            if (migrations.size() > 0) {
                for (Migration migration : migrations) {
                    if (migration.getNewIslandCell() != migration.getCurrentIslandCell()) {
                        Animal migratingAnimal = migration.getAnimal();
                        IslandCell currentIslandCell = migration.getCurrentIslandCell();
                        IslandCell newIslandCell = migration.getNewIslandCell();

                        migratingAnimal.setCurrentIslandCellX(newIslandCell.getX());
                        migratingAnimal.setCurrentIslandCellY(newIslandCell.getY());
                        newIslandCell.addCreature(migration.getAnimal());
                        currentIslandCell.getCreatures().remove(migratingAnimal);
                    }
                }
            }
        }

        resetMovedThisTick();
    }

    private void resetMovedThisTick() {
        for (IslandCell cell : islandCells) {
            for (Creature creature : cell.getCreatures()) {
                if (creature instanceof Animal) {
                    ((Animal) creature).setMovedThisTick(false);
                }
            }
        }
    }

    private ArrayList<Migration> getMigrations(IslandCell islandCell) {
        ArrayList<Migration> migrations = new ArrayList<>();

        for (Creature creature : islandCell.getCreatures()) {
            if (creature instanceof Animal && creature.isAlive() && !((Animal) creature).isMovedThisTick()) {
                Animal animal = (Animal) creature;
                Migration migration = animal.createMigration();

                if (migration != null) {
                    migrations.add(migration);
                }
            }
        }

        return migrations;
    }
}
