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
    public void eat() {
        if (isAlive()) {
            super.eat();

            IslandCell currentIslandCell = IslandCellUtil.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
            Map<Class<? extends Creature>, Integer> possibleFoodTable = getConsumptionTable()
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() != 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            List<Creature> possibleCreaturesToEat = currentIslandCell.getCreatures().stream()
                    .filter(c -> possibleFoodTable.containsKey(c.getClass())).collect(Collectors.toList());

            while (getRemainingHunger() > 0 &&
                    isAtePlantLastTime() &&
                    possibleCreaturesToEat.size() > 0 &&
                    getRequiredFood() != 0) {
                super.eat();
            }
        }

        //**********************************************
//        int randomCreatureIndex = ThreadLocalRandom.current().nextInt(0, possibleCreaturesToEat.size());
//        Creature prey = possibleCreaturesToEat.get(randomCreatureIndex);
//        String preyClassName = prey.getClass().getSimpleName();
//        boolean isPreyAlive = prey.isAlive();
//
//        int tableChance = possibleFoodTable.get(prey.getClass());
//        int actualChance = ThreadLocalRandom.current().nextInt(0, 101);
//
//        if (!prey.isAlive()) {
//            actualChance = 100;
//        }
//
//
//        List<Creature> possibleCreaturesToEat = currentIslandCell.getCreatures().stream()
//                .filter(c -> possibleFoodTable.containsKey(c.getClass())).collect(Collectors.toList());
//        ArrayList<Creature> initialPlants = currentIslandCell.getCreatures()
//                .stream()
//                .filter(c -> c instanceof Plant)
//                .collect(Collectors.toCollection(ArrayList::new));
//        float biomassOfIsland = Plant.getBioMassOfIslandCell(currentIslandCell);
//        float requiredFood = getRequiredFood();
//        float foodCounter = getRequiredFood();
//
//        if (biomassOfIsland >= requiredFood) {
//            while (foodCounter != 0f) {
//                for (Creature plant : initialPlants) {
//                    if (foodCounter >= plant.getCurrentWeight()) {
//                        foodCounter -= plant.getCurrentWeight();
//                        plant.setCurrentWeight(0);
//                        currentIslandCell.getCreatures().remove(plant);
//                    }
//                    if (foodCounter == 0) {
//                        System.out.println(
//                                getClass().getSimpleName() + " with id: " + getId() + " ate: " +
//                                        requiredFood + " kg plants. Remaining plants: " +
//                                        Plant.getBioMassOfIslandCell(currentIslandCell) + " kg.");
//                        return;
//                    }
//                    if (foodCounter < plant.getCurrentWeight() && foodCounter != 0f) {
//                        plant.setCurrentWeight(plant.getCurrentWeight() - foodCounter);
//                        plant.setAlive(false);
//                        setTicksToStarvingLeft(getMaxTicksToStarving());
//                        System.out.println(
//                                getClass().getSimpleName() + " with id: " + getId() + " ate: " +
//                                        requiredFood + " kg plants. Remaining plants: " +
//                                        Plant.getBioMassOfIslandCell(currentIslandCell) + " kg."
//                        );
//                        return;
//                    }
//                }
//            }
//        } else {
//            decrementDaysToStarvation(currentIslandCell, null);
//        }
        //TODO: implement Mouse, Boar and Duck eat not only Plant
        //**********************************************
    }
}
