package org.example.model.creature.animal.predator;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Bear extends Predator {
    public Bear(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(500);
        setMovementSpeed(2);
        setRequiredFood(80);
        setMaxPopulation(5);
        setConsumptionTable(
                createConsumptionTable(0, 80, 0, 0, 0, 40, 80, 80,
                        90, 70, 70, 50, 20, 10, 0, 0)
        );
    }

    @Override
    public void move() {

    }
}
