package org.example.model.creature.animal.predator;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Wolf extends Predator {

    public Wolf(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(50);
        setMovementSpeed(3);
        setRequiredFood(8);
        setMaxPopulation(30);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 10, 15, 60,
                        80, 60, 70, 15, 10, 40, 0, 0)
        );
    }

    @Override
    public void move() {

    }
}
