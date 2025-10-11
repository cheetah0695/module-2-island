package org.example.model.creature.animal.predator;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Eagle extends Predator {
    public Eagle(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(6);
        setCurrentWeight(6);
        setMaxMovementRange(3);
        setRequiredFood(1);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(20);
        setConsumptionTable(createConsumptionTable(0, 0, 10, 0, 0, 0, 0,
                90, 90, 0, 0, 0, 0, 80, 0, 0)
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
