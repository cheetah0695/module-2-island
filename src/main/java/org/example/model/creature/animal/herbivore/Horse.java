package org.example.model.creature.animal.herbivore;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Horse extends Herbivore {
    public Horse(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(400);
        setCurrentWeight(400);
        setMovementSpeed(4);
        setRequiredFood(60);
        setMaxPopulation(20);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 100)
        );
    }

    @Override
    public void move() {

    }
}
