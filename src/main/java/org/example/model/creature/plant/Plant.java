package org.example.model.creature.plant;

import org.example.model.creature.Creature;
import org.example.model.island.IslandCell;

public class Plant extends Creature {
    public Plant(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(1f);
        setCurrentWeight(1f);
        setMaxPopulation(200);
    }

    @Override
    public void reproduce(IslandCell islandCell) {
        try {
            if (isAlive()) {
                if (!checkPopulationFull(islandCell)) {
                    Creature newPlant = new Plant(getCurrentIslandCellX(), getCurrentIslandCellY());
                    islandCell.addCreature(newPlant);
                    System.out.println("New " + getClass().getSimpleName() + " (with the id: " + newPlant.getId() + ") has been born");
                } else {
                    System.out.println("New " + getClass().getSimpleName() + " can not be born. There are already too many!");
                }
            } else {
                System.out.println("New " + getClass().getSimpleName() + " can not be born. Parent is dead :(");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }

    @Override
    public void eat() {
        System.out.println("Plants don not eat :)");
    }

    public static float getBioMassOfIslandCell(IslandCell islandCell) {
        return islandCell.getCreatures()
                .stream()
                .filter(c -> c instanceof Plant)
                .map(Creature::getCurrentWeight)
                .reduce(0f, (w1, w2) -> w1 + w2);
    }

    private boolean checkPopulationFull(IslandCell islandCell) {
        return islandCell
                .getCreatures()
                .stream()
                .filter(c -> c.getClass() == getClass())
                .count() == getMaxPopulation();
    }
}
