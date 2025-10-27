package org.example.model.creature.animal.predator;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Fox extends Predator {
    public Fox(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        String classNameLower = getClass().getSimpleName().toLowerCase();

        setMaxWeight(config.getInt(classNameLower + ".maxWeight"));
        setCurrentWeight(config.getInt(classNameLower + ".maxWeight"));
        setMaxMovementRange(config.getInt(classNameLower + ".maxMovementRange"));
        setRequiredFood(config.getInt(classNameLower + ".requiredFood"));
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(config.getInt(classNameLower + ".maxPopulation"));
        setConsumptionTable(
                createConsumptionTable(classNameLower)
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
