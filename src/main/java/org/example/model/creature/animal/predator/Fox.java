package org.example.model.creature.animal.predator;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Fox extends Predator {
    public Fox(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(8);
        setCurrentWeight(8);
        setRequiredFood(2);
        setRemainingHunger(getRequiredFood());
        setMaxMovementRange(2);
        setMaxPopulation(30);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 70,
                        90, 0, 0, 0, 0, 60, 40, 0)
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
