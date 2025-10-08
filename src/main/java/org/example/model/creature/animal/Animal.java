package org.example.model.creature.animal;

import org.example.model.creature.Creature;
import org.example.model.creature.animal.herbivore.Herbivore;
import org.example.model.creature.animal.predator.Predator;
import org.example.model.creature.plant.Plant;
import org.example.model.island.IslandCell;
import org.example.utils.IslandCellUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Animal extends Creature {
    private int movementSpeed;
    private float requiredFood;
    private float remainingHunger;
    private int maxTicksToStarving;
    private int ticksToStarvingLeft;
    private Map<Class<? extends Creature>, Integer> consumptionTable;
    boolean atePlantLastTime;

    public Animal(int currentIslandCellX, int currentIslandCellY) {
        super(currentIslandCellX, currentIslandCellY);
        setMaxTicksToStarving(3);
        setTicksToStarvingLeft(3);
        setRemainingHunger(0);
        setAtePlantLastTime(false);
    }

    @Override
    public void reproduce(IslandCell islandCell) {
        ArrayList<Creature> creatures = islandCell.getCreatures();
        boolean sameSpecieAvailable = creatures
                .stream()
                .anyMatch(c -> c != this && c.getClass() == getClass());

        try {
            if (isAlive()) {
                if (sameSpecieAvailable) {
                    if (!checkPopulationFull(islandCell)) {
                        Creature newCreature = getClass()
                                .getDeclaredConstructor(int.class, int.class)
                                .newInstance(getCurrentIslandCellX(), getCurrentIslandCellY());
                        islandCell.addCreature(newCreature);
                        System.out.println("New creature " + getClass().getSimpleName() + " (with the id: " + newCreature.getId() + ") has been born");
                    } else {
                        System.out.println("New " + getClass().getSimpleName() + " can not be born. There are too many of them!");
                    }
                } else {
                    System.out.println("New " + getClass().getSimpleName() + " can not be born. There creature is alone!");
                }
            } else {
                System.out.println("New " + getClass().getSimpleName() + " can not be born. Parent is dead :(");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new: " + getClass().getSimpleName(), e);
        }
    }

    public void decrementDaysToStarvation(IslandCell currentIslandCell, Creature prey) {
        setTicksToStarvingLeft(getTicksToStarvingLeft() - 1);

        if (getTicksToStarvingLeft() == 0) {
            currentIslandCell.getCreatures().remove(this);
            System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") starved to death :(");
        } else {
            if (this instanceof Predator || ((this instanceof EatCaterpillar || this instanceof EatMouse) &&
                    !isAtePlantLastTime())
            ) {
                if (prey != null) {
                    System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                            ") had unsuccessfully hunted the " + prey.getClass().getSimpleName() +
                            " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() +
                            ". Remaining days to starvation: " + getTicksToStarvingLeft());
                } else {
                    System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                            "): had unsuccessfully hunted because there is no prey in the area [" +
                            getCurrentIslandCellX() + "," + getCurrentIslandCellY() + "]. Remaining hunger: " +
                            getRemainingHunger() + ". Remaining days to starvation: " +
                            getTicksToStarvingLeft());
                }
            }
            else {
                System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                        "): not enough food. Remaining hunger: " +
                        getRemainingHunger() + ". Remaining days to starvation: " +
                        getTicksToStarvingLeft());
            }
        }
    }

    public void eat() {
        if (!isAlive()) {
            return;
        }

        if (getRemainingHunger() == 0) {
            System.out.println(getClass().getSimpleName() + " (id: " + getId() +
                    ") refuses to hunt because it doesn't need food right now"
            );
            return;
        }

        IslandCell currentIslandCell = IslandCellUtil.getIslandCell(getCurrentIslandCellX(), getCurrentIslandCellY());
        Map<Class<? extends Creature>, Integer> possibleFoodTable = getConsumptionTable()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Creature> possibleCreaturesToEat = currentIslandCell.getCreatures().stream()
                .filter(c -> possibleFoodTable.containsKey(c.getClass())).collect(Collectors.toList());

        if (possibleCreaturesToEat.isEmpty()) {
            decrementDaysToStarvation(currentIslandCell, null);
            return;
        }

        int randomCreatureIndex = ThreadLocalRandom.current().nextInt(0, possibleCreaturesToEat.size());
        Creature prey = possibleCreaturesToEat.get(randomCreatureIndex);
        String preyClassName = prey.getClass().getSimpleName();
        boolean isPreyAlive = prey.isAlive();

        int tableChance = possibleFoodTable.get(prey.getClass());
        int actualChance = ThreadLocalRandom.current().nextInt(0, 101);

        if (!prey.isAlive()) {
            actualChance = 100;
        }

        if (actualChance > tableChance) {
            decrementDaysToStarvation(currentIslandCell, prey);
        } else {
            if (prey instanceof Plant) {
                setAtePlantLastTime(true);
            } else {
                setAtePlantLastTime(false);
            }

            if (prey.getCurrentWeight() >= getRemainingHunger()) {
                prey.setAlive(false);
                prey.setCurrentWeight(prey.getCurrentWeight() - getRemainingHunger());

                setTicksToStarvingLeft(getMaxTicksToStarving());
                setRemainingHunger(0);

                String firstPartMessage = getClass().getSimpleName() + " (id: " + getId();
                String howMuchEaten = prey.getCurrentWeight() == 0 ? "completely" : "partly";
                String howMuchMeatLeft = prey.getCurrentWeight() == 0 ? "" : preyClassName +
                        " meat remained: " + prey.getCurrentWeight();
                firstPartMessage += isPreyAlive ? ") killed and " + howMuchEaten + " ate " : ") " + howMuchEaten + " ate dead ";
                String secondPartMessage = preyClassName + " (id: " + prey.getId() + ")." + howMuchMeatLeft + " Remaining hunger: " + getRemainingHunger() + ". Days to starvation: " +
                        getTicksToStarvingLeft();

                System.out.println(firstPartMessage + secondPartMessage);

                if (prey.getCurrentWeight() == 0) {
                    System.out.println("*** prey is removed ***");
                    currentIslandCell.getCreatures().remove(prey);
                }
            } else {
                setTicksToStarvingLeft(getTicksToStarvingLeft() - 1);
                setRemainingHunger(getRemainingHunger() - prey.getCurrentWeight());
                currentIslandCell.getCreatures().remove(prey);

                System.out.println(getClass().getSimpleName() + " (id: " + getId() + ") completely ate " +
                        preyClassName + " (id: " + prey.getId() + "). Remaining hunger: " + getRemainingHunger() + ". Days to starvation: "
                        + getTicksToStarvingLeft());
            }
        }
    }

    //TODO: implement move() method
    public void move() {

    }

    private boolean checkPopulationFull(IslandCell islandCell) {
        return islandCell.getCreatures().stream().filter(c -> c.getClass() == getClass()).count() == getMaxPopulation();
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        if (movementSpeed > 0) {
            this.movementSpeed = movementSpeed;
        }
    }

    public float getRequiredFood() {
        return requiredFood;
    }

    public void setRequiredFood(float requiredFood) {
        if (requiredFood > 0) {
            this.requiredFood = requiredFood;
        }
    }

    public int getMaxTicksToStarving() {
        return maxTicksToStarving;
    }

    public void setMaxTicksToStarving(int maxTicksToStarving) {
        if (maxTicksToStarving > 0) {
            this.maxTicksToStarving = maxTicksToStarving;
        }
    }

    public int getTicksToStarvingLeft() {
        return ticksToStarvingLeft;
    }

    public void setTicksToStarvingLeft(int ticksToStarvingLeft) {
        if (ticksToStarvingLeft >= 0) {
            this.ticksToStarvingLeft = ticksToStarvingLeft;
        }
    }

    public Map<Class<? extends Creature>, Integer> getConsumptionTable() {
        return consumptionTable;
    }

    public void setConsumptionTable(Map<Class<? extends Creature>, Integer> consumptionTable) {
        this.consumptionTable = consumptionTable;
    }

    public float getRemainingHunger() {
        return remainingHunger;
    }

    public void setRemainingHunger(float remainingHunger) {
        if (remainingHunger >= 0 && remainingHunger <= requiredFood) {
            this.remainingHunger = remainingHunger;
        } else if (remainingHunger > requiredFood) {
            this.remainingHunger = requiredFood;
        } else {
            this.remainingHunger = 0;
        }
    }

    public boolean isAtePlantLastTime() {
        return atePlantLastTime;
    }

    public void setAtePlantLastTime(boolean atePlantLastTime) {
        this.atePlantLastTime = atePlantLastTime;
    }
}
