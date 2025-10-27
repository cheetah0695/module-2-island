package org.example.model.creature.animal.herbivore;

import org.example.model.creature.animal.EatCaterpillar;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Mouse extends Herbivore implements EatCaterpillar {
    public Mouse(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        String classNameLower = getClass().getSimpleName().toLowerCase();

        setMaxWeight(config.getFloat(classNameLower + ".maxWeight"));
        setCurrentWeight(config.getFloat(classNameLower + ".maxWeight"));
        setMaxMovementRange(config.getInt(classNameLower + ".maxMovementRange"));
        setRequiredFood(config.getFloat(classNameLower + ".requiredFood"));
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
