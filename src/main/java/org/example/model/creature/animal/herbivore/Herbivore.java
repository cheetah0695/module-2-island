package org.example.model.creature.animal.herbivore;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.island.IslandCell;
import org.example.utils.IslandCellUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Herbivore extends Animal {
    public Herbivore(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
    }

    @Override
    public void tryToEat(Creature creature) {
        do {
            super.tryToEat(creature);
        } while (getRemainingHunger() > 0 &&
                isAtePlantLastTime() && creature.getCurrentWeight() > 0);
    }
}
