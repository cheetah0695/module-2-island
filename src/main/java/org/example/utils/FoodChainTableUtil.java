package org.example.utils;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.herbivore.Boar;
import org.example.model.creature.animal.herbivore.Bull;
import org.example.model.creature.animal.herbivore.Caterpillar;
import org.example.model.creature.animal.herbivore.Deer;
import org.example.model.creature.animal.herbivore.Duck;
import org.example.model.creature.animal.herbivore.Goat;
import org.example.model.creature.animal.herbivore.Horse;
import org.example.model.creature.animal.herbivore.Mouse;
import org.example.model.creature.animal.herbivore.Rabbit;
import org.example.model.creature.animal.herbivore.Sheep;
import org.example.model.creature.animal.predator.Bear;
import org.example.model.creature.animal.predator.Eagle;
import org.example.model.creature.animal.predator.Fox;
import org.example.model.creature.animal.predator.Snake;
import org.example.model.creature.animal.predator.Wolf;
import org.example.model.creature.plant.Plant;

import java.util.HashMap;

public class FoodChainTableUtil {
    private static final Config config = Config.getInstance();

    public static HashMap<Class<? extends Creature>, Integer> createConsumptionTable(String classNameLower) {
        HashMap<Class<? extends Creature>, Integer> consumptionTable = new HashMap<>();
        consumptionTable.put(Wolf.class, config.getInt(classNameLower + ".eat-wolf"));
        consumptionTable.put(Snake.class, config.getInt(classNameLower + ".eat-snake"));
        consumptionTable.put(Fox.class, config.getInt(classNameLower + ".eat-fox"));
        consumptionTable.put(Bear.class, config.getInt(classNameLower + ".eat-bear"));
        consumptionTable.put(Eagle.class, config.getInt(classNameLower + ".eat-eagle"));
        consumptionTable.put(Horse.class, config.getInt(classNameLower + ".eat-horse"));
        consumptionTable.put(Deer.class, config.getInt(classNameLower + ".eat-deer"));
        consumptionTable.put(Rabbit.class, config.getInt(classNameLower + ".eat-rabbit"));
        consumptionTable.put(Mouse.class, config.getInt(classNameLower + ".eat-mouse"));
        consumptionTable.put(Goat.class, config.getInt(classNameLower + ".eat-goat"));
        consumptionTable.put(Sheep.class, config.getInt(classNameLower + ".eat-sheep"));
        consumptionTable.put(Boar.class, config.getInt(classNameLower + ".eat-boar"));
        consumptionTable.put(Bull.class, config.getInt(classNameLower + ".eat-bull"));
        consumptionTable.put(Duck.class, config.getInt(classNameLower + ".eat-duck"));
        consumptionTable.put(Caterpillar.class, config.getInt(classNameLower + ".eat-caterpillar"));
        consumptionTable.put(Plant.class, config.getInt(classNameLower + ".eat-plant"));

        return consumptionTable;
    }
}
