package org.example.model.creature.animal.herbivore;

import org.example.model.creature.animal.EatCaterpillar;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Mouse extends Herbivore implements EatCaterpillar {
    public Mouse(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(0.05f);
        setCurrentWeight(0.05f);
        setMaxMovementRange(1);
        setRequiredFood(0.01f);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(500);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 90, 100)
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
