package org.example.model.creature.animal.herbivore;

import org.example.model.creature.animal.EatCaterpillar;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Mouse extends Herbivore implements EatCaterpillar {
    public Mouse(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(0.05f);
        setMovementSpeed(1);
        setRequiredFood(0.01f);
        setMaxPopulation(500);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 90, 100)
        );
    }

    @Override
    public void move() {

    }
}
