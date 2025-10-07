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
    public static HashMap<Class<? extends Creature>, Integer> createConsumptionTable(
            int wolfL, int snakeL, int foxL, int bearL, int eagleL, int horseL, int deerL, int rabbitL, int mouseL,
            int goatL, int sheepL, int boarL, int bullL, int duckL, int caterpillarL, int plantL
    ) {
        HashMap<Class<? extends Creature>, Integer> consumptionTable = new HashMap<>();
        consumptionTable.put(Wolf.class, wolfL);
        consumptionTable.put(Snake.class, snakeL);
        consumptionTable.put(Fox.class, foxL);
        consumptionTable.put(Bear.class, bearL);
        consumptionTable.put(Eagle.class, eagleL);
        consumptionTable.put(Horse.class, horseL);
        consumptionTable.put(Deer.class, deerL);
        consumptionTable.put(Rabbit.class, rabbitL);
        consumptionTable.put(Mouse.class, mouseL);
        consumptionTable.put(Goat.class, goatL);
        consumptionTable.put(Sheep.class, sheepL);
        consumptionTable.put(Boar.class, boarL);
        consumptionTable.put(Bull.class, bullL);
        consumptionTable.put(Duck.class, duckL);
        consumptionTable.put(Caterpillar.class, caterpillarL);
        consumptionTable.put(Plant.class, plantL);

        return consumptionTable;
    }
}
