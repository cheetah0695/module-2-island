package org.example.model.creature.animal.predator;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.island.IslandCell;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Predator extends Animal {
    public Predator(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
    }

    @Override
    public void eat(IslandCell islandCell) {
        List<Creature> creaturesToEat = islandCell.getCreatures().stream()
                .filter(c -> c.getClass() != getClass()).collect(Collectors.toList());

        if (creaturesToEat.isEmpty()) {
            System.out.println("Nothing to eat!");
            return;
        }

        int randomCreatureIndex = ThreadLocalRandom.current().nextInt(0, creaturesToEat.size());
        Creature randomCreature = creaturesToEat.get(randomCreatureIndex);

        int chanceToEat = getConsumptionTable().get(randomCreature.getClass());
        int randomChance = ThreadLocalRandom.current().nextInt(0, 101);

        if (chanceToEat == 0 || randomChance < chanceToEat) {
            System.out.println(getClass().getSimpleName() + " could not eat " +
                    randomCreature.getClass().getSimpleName());
        } else {
            islandCell.getCreatures().remove(randomCreature);

            if (randomCreature.getWeight() >= getRequiredFood()) {
                setTicksToStarvingLeft(getMaxTicksToStarving());
            }
            System.out.println(getClass().getSimpleName() + " is eating " +
                    randomCreature.getClass().getSimpleName());
        }
    }
}
