package org.example.model.creature.animal.predator;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Snake extends Predator {
    public Snake(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(15);
        setCurrentWeight(15);
        setMaxMovementRange(1);
        setRequiredFood(3);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(30);
        setConsumptionTable(
                createConsumptionTable(0, 0, 15, 0, 0, 0, 0, 20,
                        40, 0, 0, 0, 0, 10, 0, 0)
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
