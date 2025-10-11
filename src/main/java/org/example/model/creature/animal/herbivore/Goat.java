package org.example.model.creature.animal.herbivore;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Goat extends Herbivore {
    public Goat(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(60);
        setCurrentWeight(60);
        setMaxMovementRange(3);
        setRequiredFood(10);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(140);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 100)
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
