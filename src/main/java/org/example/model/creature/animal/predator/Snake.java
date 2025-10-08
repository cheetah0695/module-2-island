package org.example.model.creature.animal.predator;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Snake extends Predator {
    public Snake(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(15);
        setCurrentWeight(15);
        setMovementSpeed(1);
        setRequiredFood(3);
        setMaxPopulation(30);
        setConsumptionTable(
                createConsumptionTable(0, 0, 15, 0, 0, 0, 0, 20,
                        40, 0, 0, 0, 0, 10, 0, 0)
        );
    }

    @Override
    public void move() {

    }
}
