package org.example.model.creature.animal.herbivore;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Bull extends Herbivore {
    public Bull(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(700);
        setMovementSpeed(3);
        setRequiredFood(100);
        setMaxPopulation(10);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 100)
        );
    }

    @Override
    public void move() {

    }
}
