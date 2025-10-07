package org.example.model.creature.animal.herbivore;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Sheep extends Herbivore {
    public Sheep(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(70);
        setCurrentWeight(70);
        setMovementSpeed(3);
        setRequiredFood(15);
        setMaxPopulation(140);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 100)
        );
    }

    @Override
    public void move() {

    }
}
