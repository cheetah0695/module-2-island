package org.example.model.creature.animal.herbivore;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Caterpillar extends Herbivore {
    public Caterpillar(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(0.01f);
        setCurrentWeight(0.01f);
        setMovementSpeed(0);
        setRequiredFood(0.0f);
        setMaxPopulation(1000);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 100)
        );
    }

    //TODO: caterpillars immortal!

    @Override
    public void move() {

    }
}
