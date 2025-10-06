package org.example.model.creature.animal.herbivore;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Sheep extends Herbivore {
    public Sheep(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(70);
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
