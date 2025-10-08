package org.example.model.creature.animal.herbivore;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Rabbit extends Herbivore {
    public Rabbit(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(2);
        setCurrentWeight(2);
        setMovementSpeed(2);
        setRequiredFood(0.45f);
        setMaxPopulation(150);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 100)
        );
    }

    @Override
    public void move() {

    }
}
