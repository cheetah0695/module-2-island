package org.example.model.creature.animal.herbivore;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Mouse extends Herbivore {
    public Mouse(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        String classNameLower = getClass().getSimpleName().toLowerCase();

        setMaxWeight(config.getFloat(classNameLower + ".max-weight"));
        setCurrentWeight(config.getFloat(classNameLower + ".max-weight"));
        setMaxMovementRange(config.getInt(classNameLower + ".max-movement-range"));
        setRequiredFood(config.getFloat(classNameLower + ".required-food"));
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(config.getInt(classNameLower + ".max-population"));

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
