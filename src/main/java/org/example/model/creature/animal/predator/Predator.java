package org.example.model.creature.animal.predator;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.island.IslandCell;
import org.example.utils.IslandCellUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Predator extends Animal {
    public Predator(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
    }

    @Override
    public void eat() {
        IslandCell currentIslandCell = IslandCellUtil.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
        List<Creature> creaturesToEat = currentIslandCell.getCreatures().stream()
                .filter(c -> c.getClass() != getClass()).collect(Collectors.toList());

        if (creaturesToEat.isEmpty()) {
            decrementDaysToStarvation(currentIslandCell, null);
            return;
        }

        int randomCreatureIndex = ThreadLocalRandom.current().nextInt(0, creaturesToEat.size());
        Creature prey = creaturesToEat.get(randomCreatureIndex);
        String preyClassName = prey.getClass().getSimpleName();
        boolean isPreyAlive = prey.isAlive();

        int chanceToEat = getConsumptionTable().get(prey.getClass());
        int randomChance = ThreadLocalRandom.current().nextInt(0, 101);

        if (chanceToEat != 0 && !prey.isAlive()) {
            randomChance = 100;
        }

        if (chanceToEat == 0 || randomChance < chanceToEat) {
            decrementDaysToStarvation(currentIslandCell, prey);
        } else {
            if (prey.getCurrentWeight() >= getRequiredFood()) {
                setTicksToStarvingLeft(getMaxTicksToStarving());
                prey.setAlive(false);
                prey.setCurrentWeight(prey.getCurrentWeight() - getRequiredFood());
                String firstPartMessage = getClass().getSimpleName() + " (id: " + getId();
                String secondPartMessage = preyClassName + " (id: " + prey.getId() + "). " + preyClassName +
                        " meat remained: " + prey.getCurrentWeight() + ". Days to starvation: " +
                        getTicksToStarvingLeft();

                if (isPreyAlive) {
                    firstPartMessage += ") killed and partly ate ";
                } else {
                    firstPartMessage += ") partly ate dead ";
                }

                if (prey.getCurrentWeight() == 0) {
                    currentIslandCell.getCreatures().remove(prey);
                }

                System.out.println(firstPartMessage + secondPartMessage);
            } else {
                currentIslandCell.getCreatures().remove(prey);
                System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") completely ate " +
                        preyClassName + " (id: " + prey.getId() + "). Days to starvation: "
                        + getTicksToStarvingLeft());
            }
        }
    }
}
