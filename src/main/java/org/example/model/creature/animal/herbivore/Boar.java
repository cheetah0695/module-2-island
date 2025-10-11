package org.example.model.creature.animal.herbivore;

import org.example.model.creature.animal.EatCaterpillar;
import org.example.model.creature.animal.EatMouse;

import java.util.Map;
import java.util.stream.Collectors;

import static org.example.utils.FoodChainTableUtil.createConsumptionTable;

public class Boar extends Herbivore implements EatCaterpillar, EatMouse {
    public Boar(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxWeight(400);
        setCurrentWeight(400);
        setMaxMovementRange(2);
        setRequiredFood(50);
        setRemainingHunger(getRequiredFood());
        setMaxPopulation(50);
        setConsumptionTable(
                createConsumptionTable(0, 0, 0, 0, 0, 0, 0, 0,
                        50, 0, 0, 0, 0, 0, 90, 100)
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
