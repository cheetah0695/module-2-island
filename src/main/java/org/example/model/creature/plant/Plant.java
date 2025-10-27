package org.example.model.creature.plant;

import org.example.model.creature.Creature;
import org.example.model.island.IslandCell;

public class Plant extends Creature {
    public Plant(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);        String classNameLower = getClass().getSimpleName().toLowerCase();

        setMaxWeight(config.getFloat(classNameLower + ".maxWeight"));
        setCurrentWeight(config.getFloat(classNameLower + ".maxWeight"));
        setMaxPopulation(config.getInt(classNameLower + ".maxPopulation"));
    }

    @Override
    public void reproduce(IslandCell islandCell) {
        try {
            if (isAlive()) {
                synchronized (islandCell) {
                    if (!checkPopulationFull(islandCell)) {
                        Creature newPlant = new Plant(getCurrentIslandCellX(), getCurrentIslandCellY());
                        islandCell.addCreature(newPlant);
                        //                    System.out.println("New " + getClass().getSimpleName() + " (with the id: " + newPlant.getId() + ") has been born");
                    } else {
                        //                    System.out.println("New " + getClass().getSimpleName() + " can not be born. There are already too many!");
                    }
                }
            } else {
//                System.out.println("New " + getClass().getSimpleName() + " can not be born. Parent is dead :(");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }

    @Override
    public void tryToEat(Creature creature) {
        System.out.println("Plants don not eat :)");
    }

    private boolean checkPopulationFull(IslandCell islandCell) {
        synchronized(islandCell) {
            return islandCell
                    .getCreatures()
                    .stream()
                    .filter(c -> c.getClass() == getClass())
                    .count() == getMaxPopulation();
        }
    }
}
