package org.example.model.creature.animal.predator;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Bear extends Predator {
    public Bear(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(500);
        setCurrentWeight(500);
        setMaxMovementRange(2);
        setRequiredFood(80);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(5);
        setConsumptionTable(
                createConsumptionTable(0, 80, 0, 0, 0, 40, 80, 80,
                        90, 70, 70, 50, 20, 10, 0, 0)
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
