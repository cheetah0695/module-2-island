package org.example.model.creature.animal.predator;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Eagle extends Predator {
    public Eagle(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(6);
        setMovementSpeed(3);
        setRequiredFood(1);
        setMaxPopulation(20);
        setConsumptionTable(createConsumptionTable(0, 0, 10, 0, 0, 0, 0,
                90, 90, 0, 0, 0, 0, 80, 0, 0)
        );
    }

    @Override
    public void move() {

    }
}
