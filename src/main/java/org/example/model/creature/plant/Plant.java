package org.example.model.creature.plant;

import org.example.model.creature.Creature;
import org.example.model.island.IslandCell;

import java.util.List;

public class Plant extends Creature {
    public Plant(int currentIslandCellX, int currentIslandCellY) {
        setCurrentIslandCellX(currentIslandCellX);
        setCurrentIslandCellY(currentIslandCellY);
        setWeight(1);
        setMaxPopulation(200);
        setId(Creature.getMaxId() + 1);
        Creature.setMaxId(Creature.getMaxId() + 1);
    }

    @Override
    public void reproduce(IslandCell islandCell, List<Creature> newborns) {
        try {
            if (!checkPopulationFull(islandCell)) {
                newborns.add(new Plant(getCurrentIslandCellX(), getCurrentIslandCellY()));
                System.out.println("New creature " + getClass().getSimpleName() + " (with the id: " + getId() + ") has been born");
            } else {
                System.out.println(getClass().getSimpleName() + " can not born. There are already too many!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }

    private boolean checkPopulationFull(IslandCell islandCell) {
        return islandCell
                .getCreatures()
                .stream()
                .filter(c -> c.getClass() == getClass())
                .count() == getMaxPopulation();
    }
}
