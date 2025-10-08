package org.example.model.creature.animal.herbivore;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Deer extends Herbivore {
    public Deer(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(300);
        setCurrentWeight(300);
        setMovementSpeed(4);
        setRequiredFood(50);
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
