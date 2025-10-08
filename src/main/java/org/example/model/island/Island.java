package org.example.model.island;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.creature.animal.herbivore.Caterpillar;
import org.example.model.creature.animal.herbivore.Herbivore;
import org.example.model.creature.animal.predator.Predator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Island {
    private static ArrayList<IslandCell> islandCells;

    public Island(int sizeX, int sizeY) {
        islandCells = new ArrayList<>();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                islandCells.add(new IslandCell(i, j));
            }
        }
    }

    public static ArrayList<IslandCell> getIslandCells() {
        return islandCells;
    }

    public void runSimulation() {
//        reproduce();

        List<Creature> animals = islandCells.stream()
                .map(ic -> ic.getCreatures())
                .flatMap(List::stream)
                .filter(c -> c instanceof Animal && c.isAlive())
                .collect(Collectors.toList());

        while (animals.size() > 0 && !animals.stream().allMatch(c -> c instanceof Caterpillar)) {
            eat();
            animals = islandCells.stream()
                    .map(ic -> ic.getCreatures())
                    .flatMap(List::stream)
                    .filter(c -> c instanceof Animal && c.isAlive())
                    .collect(Collectors.toList());
            animals.forEach(animal -> {
                ((Animal) animal).setRemainingHunger(((Animal) animal).getRemainingHunger() + 3);
            });
        }
    }

    private void eat() {
        for (IslandCell islandCell : islandCells) {
            List<Creature> animals = islandCell.getCreatures()
                    .stream()
                    .filter(c -> c instanceof Animal && c.isAlive())
                    .collect(Collectors.toList());

            for (int i = 0; i < animals.size(); i++) {
                Creature creature = islandCell.getCreatures().get(i);
                if (creature.isAlive()) {
                    creature.eat();
                    //TODO: dead creatures eat, because used the old List<Creature> animals
                }
            }
        }

        for (IslandCell islandCell : islandCells) {
            List<Creature> herbivores = islandCell.getCreatures()
                    .stream()
                    .filter(c -> c instanceof Herbivore && c.isAlive())
                    .collect(Collectors.toList());

            System.out.println("Herbivores left: " + herbivores.size());

            System.out.print("Herbivores left with ids: ");
            for (Creature herbivore : herbivores) {
                System.out.print(herbivore.getId() + ",");
            }
            System.out.println();
        }

        for (IslandCell islandCell : islandCells) {
            List<Creature> predators = islandCell.getCreatures()
                    .stream()
                    .filter(c -> c instanceof Predator && c.isAlive())
                    .collect(Collectors.toList());

            System.out.println("Predators left: " + predators.size());

            System.out.print("Predators left with ids: ");
            for (Creature predator : predators) {
                System.out.print(predator.getId() + ",");
            }
            System.out.println();
        }
    }

    private void reproduce() {
        for (IslandCell islandCell : islandCells) {
            System.out.println("Start of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");

            ArrayList<Creature> initialList = new ArrayList<>(islandCell.getCreatures());

            for (Creature creature : initialList) {
                creature.reproduce(islandCell);
            }

            System.out.println("End of reproducing for the island cell: [" + islandCell.getX() + "," + islandCell.getY() + "]");
        }
    }
}
