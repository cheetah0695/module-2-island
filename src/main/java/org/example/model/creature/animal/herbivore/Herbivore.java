package org.example.model.creature.animal.herbivore;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.Animal;
import org.example.model.creature.plant.Plant;
import org.example.model.island.IslandCell;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Herbivore extends Animal {
    public Herbivore(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
    }

    @Override
    public void eat(IslandCell islandCell) {
        ArrayList<Creature> creatures = islandCell.getCreatures();
        long plantQuantity = creatures.stream().filter(c -> c instanceof Plant).count();
        Iterator<Creature> iterator = creatures.iterator();

        if (plantQuantity >= getRequiredFood()) {
            int counter = 0;
            while (iterator.hasNext() && counter != getRequiredFood()) {
                Creature creature = iterator.next();
                if (creature instanceof Plant) {
                    iterator.remove();
                    counter++;
                }
            }

            System.out.println(this.getClass().getSimpleName() + " is eating " + getRequiredFood() + " plants");
        } else {
            System.out.println(this.getClass().getSimpleName() + ": not enough plants to eat");
        }
    }
}
