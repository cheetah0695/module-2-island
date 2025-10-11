package org.example.model.creature.animal.predator;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Wolf extends Predator {

    public Wolf(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(50);
        setCurrentWeight(50);
        setMaxMovementRange(3);
        setRequiredFood(8);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(30);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 10, 15, 60,
                        80, 60, 70, 15, 10, 40, 0, 0)
        );
        setPossibleFoodTable(
                getConsumptionTable()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue() != 0)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
