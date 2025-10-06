package org.example.model.creature.animal.herbivore;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Caterpillar extends Herbivore {
    public Caterpillar(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(0.01f);
        setMovementSpeed(0);
        setRequiredFood(0);
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
