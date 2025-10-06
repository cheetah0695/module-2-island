package org.example.model.creature.animal.herbivore;

import org.example.model.creature.animal.EatCaterpillar;

import static org.example.utils.Comsumption.createConsumptionTable;

public class Duck extends Herbivore implements EatCaterpillar {
    public Duck(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setWeight(1);
        setMovementSpeed(4);
        setRequiredFood(0.15f);
        setMaxPopulation(200);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 90, 100)
        );
    }
//TODO: Duck eats caterpillars and grass!!!

    @Override
    public void move() {

    }
}
