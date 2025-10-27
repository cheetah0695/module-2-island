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

    //    public static HashMap<Class<? extends Creature>, Integer> createConsumptionTable(
//            int wolfL, int snakeL, int foxL, int bearL, int eagleL, int horseL, int deerL, int rabbitL, int mouseL,
//            int goatL, int sheepL, int boarL, int bullL, int duckL, int caterpillarL, int plantL
//    ) {
//        HashMap<Class<? extends Creature>, Integer> consumptionTable = new HashMap<>();
//        consumptionTable.put(Wolf.class, wolfL);
//        consumptionTable.put(Snake.class, snakeL);
//        consumptionTable.put(Fox.class, foxL);
//        consumptionTable.put(Bear.class, bearL);
//        consumptionTable.put(Eagle.class, eagleL);
//        consumptionTable.put(Horse.class, horseL);
//        consumptionTable.put(Deer.class, deerL);
//        consumptionTable.put(Rabbit.class, rabbitL);
//        consumptionTable.put(Mouse.class, mouseL);
//        consumptionTable.put(Goat.class, goatL);
//        consumptionTable.put(Sheep.class, sheepL);
//        consumptionTable.put(Boar.class, boarL);
//        consumptionTable.put(Bull.class, bullL);
//        consumptionTable.put(Duck.class, duckL);
//        consumptionTable.put(Caterpillar.class, caterpillarL);
//        consumptionTable.put(Plant.class, plantL);
//
//        return consumptionTable;
//    }
    public static HashMap<Class<? extends Creature>, Integer> createConsumptionTable(String classNameLower) {
        HashMap<Class<? extends Creature>, Integer> consumptionTable = new HashMap<>();
        consumptionTable.put(Wolf.class, config.getInt(classNameLower + ".eatWolf"));
        consumptionTable.put(Snake.class, config.getInt(classNameLower + ".eatSnake"));
        consumptionTable.put(Fox.class, config.getInt(classNameLower + ".eatFox"));
        consumptionTable.put(Bear.class, config.getInt(classNameLower + ".eatBear"));
        consumptionTable.put(Eagle.class, config.getInt(classNameLower + ".eatEagle"));
        consumptionTable.put(Horse.class, config.getInt(classNameLower + ".eatHorse"));
        consumptionTable.put(Deer.class, config.getInt(classNameLower + ".eatDeer"));
        consumptionTable.put(Rabbit.class, config.getInt(classNameLower + ".eatRabbit"));
        consumptionTable.put(Mouse.class, config.getInt(classNameLower + ".eatMouse"));
        consumptionTable.put(Goat.class, config.getInt(classNameLower + ".eatGoat"));
        consumptionTable.put(Sheep.class, config.getInt(classNameLower + ".eatSheep"));
        consumptionTable.put(Boar.class, config.getInt(classNameLower + ".eatBoar"));
        consumptionTable.put(Bull.class, config.getInt(classNameLower + ".eatBull"));
        consumptionTable.put(Duck.class, config.getInt(classNameLower + ".eatDuck"));
        consumptionTable.put(Caterpillar.class, config.getInt(classNameLower + ".eatCaterpillar"));
        consumptionTable.put(Plant.class, config.getInt(classNameLower + ".eatPlant"));

        return consumptionTable;
    }
}
