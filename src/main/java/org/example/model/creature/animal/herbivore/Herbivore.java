package org.example.model.creature.animal.herbivore;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.creature.plant.Plant;
import org.example.model.island.IslandCell;
import org.example.utils.IslandCellUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class Herbivore extends Animal {
    public Herbivore(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
    }

    @Override
    public void eat() {
        IslandCell currentIslandCell = IslandCellUtil.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
        ArrayList<Creature> initialPlants = currentIslandCell.getCreatures()
                .stream()
                .filter(c -> c instanceof Plant)
                .collect(Collectors.toCollection(ArrayList::new));
        float biomassOfIsland = Plant.getBioMassOfIslandCell(currentIslandCell);
        float requiredFood = getRequiredFood();
        float foodCounter = getRequiredFood();

        if (biomassOfIsland >= requiredFood) {
            while (foodCounter != 0f) {
                for (Creature plant : initialPlants) {
                    if (foodCounter >= plant.getCurrentWeight()) {
                        foodCounter -= plant.getCurrentWeight();
                        plant.setCurrentWeight(0);
                        currentIslandCell.getCreatures().remove(plant);
                    }
                    if (foodCounter == 0) {
                        System.out.println(
                                getClass().getSimpleName() + " with id: " + getId() + " ate: " +
                                        requiredFood + " kg plants. Remaining plants: " +
                                        Plant.getBioMassOfIslandCell(currentIslandCell) + " kg.");
                        return;
                    }
                    if (foodCounter < plant.getCurrentWeight() && foodCounter != 0f) {
                        plant.setCurrentWeight(plant.getCurrentWeight() - foodCounter);
                        plant.setAlive(false);
                        setTicksToStarvingLeft(getMaxTicksToStarving());
                        System.out.println(
                                getClass().getSimpleName() + " with id: " + getId() + " ate: " +
                                        requiredFood + " kg plants. Remaining plants: " +
                                        Plant.getBioMassOfIslandCell(currentIslandCell) + " kg."
                        );
                        return;
                    }
                }
            }
        } else {
            decrementDaysToStarvation(currentIslandCell, null);
        }
        //TODO: implement Mouse, Boar and Duck eat not only Plant
    }
}
