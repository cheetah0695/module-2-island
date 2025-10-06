package org.example.model.creature.animal.herbivore;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Rabbit extends Herbivore {
    public Rabbit(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(2);
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
