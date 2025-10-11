package org.example.model.creature.animal.herbivore;

import org.example.model.creature.animal.EatCaterpillar;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Duck extends Herbivore implements EatCaterpillar {
    public Duck(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(1);
        setCurrentWeight(1);
        setMaxMovementRange(4);
        setRequiredFood(0.15f);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(200);
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
