package org.example.model.creature.animal.herbivore;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Goat extends Herbivore {
    public Goat(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(60);
        setMovementSpeed(3);
        setRequiredFood(10);
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
