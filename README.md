<a href="#Введение">на русском</a>

## Introduction

This project is a simulation of the life of creatures (Creature) on an island (Island), which consists of cells (IslandCell).
Each cell contains a certain number of creatures.

Each creature (Creature) can be of two types: a plant (Plant) or an animal (Animal).
Animals can be either herbivores (Herbivore) or predators (Predator).
Each animal species is represented by a specific class.

Herbivore classes: Boar, Bull, Caterpillar, Deer, Duck, Goat, Horse, Mouse, Rabbit, Sheep.
Predator classes: Bear, Eagle, Fox, Snake, Wolf.

The simulation runs in cycles (called ticks), where each creature executes its own life cycle.
The number of island cells (IslandCell) is configured in the config.properties file.

## Multithreading

Multithreading is implemented using a ScheduledThreadPool of size 3 (configurable in config.properties),
in which tasks (Task) are executed.
Each task runs the .run() method of every creature (Creature) within a particular cell (IslandCell).

## Statistics (console output)

- After the island (Island) is created, the number of each creature type across all island cells (IslandCell) is displayed.

- After each cycle (tick), an aggregated count of all living creatures (plants, predators, and herbivores) is shown.

- The code also contains numerous commented-out debug print statements that were used during development.

## Simulation End Condition

The simulation ends when all predators (Predator) die.

Last run times:
- Island size 20x10 cells — 38 seconds.
- Island size 100x20 cells — 1521 seconds (~26 minutes).

## Animal (Animal) life cycle per simulation tick
### Eating

An animal searches for potential prey in its current cell based on the diet table defined in config.properties
(for example, wolf.eat-rabbit=60 means that a wolf has a 60% chance to eat a rabbit).
It randomly selects one creature and attempts to eat it.

If successful, the prey’s state changes to dead (if it was alive before the attack),
and the predator’s satiety increases by an amount equal to the prey’s mass —
but not exceeding the prey’s current mass or the predator’s maximum food capacity (defined in config.properties).

A creature can be eaten either fully or partially — the eaten mass is subtracted from its body.
The chance of eating an already dead creature increases to 100% if it was previously killed but not fully consumed.
In that case, if the prey species is part of the predator’s diet, it will be eaten (partially or completely,
depending on the predator’s hunger and the remaining mass).

- If the eater is a predator, it hunts only once per cycle.
- If the eater is a herbivore, it can eat as much as needed until fully satiated, as long as there are plants in 
the current cell.
- If the eater is a boar, mouse, or duck, the following combinations are possible within a single cycle:
  1. Eats one or more plants first, then a non-plant creature (and stops eating).
  2. Eats a non-plant creature right away (and stops eating).
  3. Eats only plants until fully satiated (if there are enough plants available).

### Starvation

If an animal’s hunger level exceeds half of its required daily food (as defined in config.properties), its ticks to 
starvation value decreases by 1 each cycle, down to zero. When it reaches 0, the animal dies of starvation.

### Reproduction

A creature can reproduce only if all the following conditions are met:

1. The maximum number of this species has not been reached in the current cell.
2. There is at least one other living creature of the same class in the same cell.
3. The creature is alive.
4. The creature is fully satiated.

### Migration

An animal can migrate to another cell if:
1. The distance is between 0 (staying in the same cell) and its maximum movement speed (defined in config.properties).
2. The target cell has not reached its population limit for that species.

### Hunger Reset

At the end of each cycle, the animal’s hunger level increases to its maximum.
Each new cycle begins with the hunger level set to the animal’s required food amount (as defined in config.properties).

## Plant (Plant) Life Cycle per Simulation Tick
### Reproduction
Same as animals, but without the requirement of being fully satiated.

## Death
### Animals (Animal) can die from:
1. Starvation — when they remain hungry beyond half their required daily food for more cycles
than allowed by the configuration (config.properties).
2. Predation — being killed by another animal (e.g., a predator).

### Plants (Plant)
Similar to animals, but cannot die from starvation.

## Decomposition (for dead creatures)

Each cycle following the creature’s death, if it hasn’t been fully eaten,
the number of days until complete decomposition (defined in config.properties) decreases.
When this value reaches zero, the creature’s remains disappear,
and they can no longer be eaten by other creatures.

## Введение
Этот проект - симуляция жизни существ (Creature) на острове (Island), который состоит из ячеек (IslandCell), 
каждая из которых имеет определённое колличество существ (Creature).

Каждое существо (Creature) бывает двух видов: растение (Plant) и животное (Animal). Животное бывает травоядным 
(Herbivore) или хищником (Predator). Каждое животное представлено конкретным классом.

Классы травоядных (Herbivore): кабан (Boar), буйвол (Bull), гусеница (Caterpillar), олень (Deer), утка (Duck), коза 
(Goat), лошадь (Horse), мышь (Mouse), кролик (Rabbit), овца (Sheep).
Классы хищников (Predator): медведь (Bear), орёл (Eagle), лиса (Fox), змея (Snake), волк (Wolf).

Симуляция происходит в циклах (tick), где каждое существо (Creature) выполняет свой жизненный цикл.
Колличество ячеек острова (IslandCell) конфигурируется в файле config.properties.

## Многопоточность
Представлена созданием scheduledPool размером 3 (настраивается в config.properties) в котором вызываются задачи (Task), которые являются запуском метода .run() у каждого существа (Creature) конкретной ячейки (IslandCell).

## Статистика (консоль)
- после создания острова (Island) выводится колличество каждого типа существа (Creature) во всех учейках острова 
(IslandCell) 
- после каждого цикла (tick) укрупнённо выводится колличество живых существ (растений (Plant), хищников (Predator) и 
травоядных (Herbivore))
- в коде имеется многочисленные закомментируемые выводы в консоль, которые служили для отладки

## Условия окончания симуляции
Смерть всех хищников (Predator).

Последний запуск симуляции размером 20x10 ячеек занял: 38 секунд.
Последний запуск симуляции размером 100x20 ячеек занял: 1521 секунд (~26 минут).

## Жизненный цикл животного (Animal) на 1 такт симуляции

### Питание
Животное ищет в своей ячейке возможную добычу, исходя из таблицы рациона из файла config.properties 
(там где значение не равно 0, например wolf.eat-rabbit=60 означает, что волк может съесть кролика с вероятность 60%)
и случайным образом выбирает одно существо и пытается его съесть. 

Если животному это удаётся, то состояние съеденного 
переходит в "мёртв" (если съеденный на момент попытки был жив), а состояние сытости добытчика увеличивается на величину 
равную массе съеденного существа, но не более текущей массы съеденного существа и не более максимальное величины 
требуемой пищи для этого животного (определено в config.properties). 

Существо может быть съедено как полностью, так и частично. Из съеденного существа вычитается съеденная масса. 

Вероятность съесть мёртвое существо увеличивается 
до 100%, в случае, если убитое существо не было полностью съедено. То есть если в рацион добытчика входит тип существа,
которого до этого убили и не полностью съели, то добытчик съест существо с вероятностью 100% частично или полностью 
(зависит от сытости добытчика и массы останков).
- Если добытчик это хищник, охота длится только 1 раз за 1 цикл.
- Если добытчик это травоядное, то оно может есть столько, сколько ему необходимо до состояния полной сытости, 
если имеется необходимое количество растений в текущей ячейке.
- Если добытчик это кабан (Boar), мышь (Mouse) или утка (Duck), возможны варианты, когда это особое травоядное в одном 
цикле съест:
  1. сначала одно растение (или несколько), а потом не растение (прекращая питание в этом цикле)
  2. сразу не растение (прекращая питание в этом цикле)
  3. всё время траву (до состояния полной сытости, и пока в текущей ячейке есть растения)

### Голодание
Если животное имеет показатель голода больший чем половина от требумой еды (определено в config.properties), 
то у него уменьшается значение циклов до смерти от голода (определено в config.properties) на 1 единицу вплоть до 0.
При значении циклов до смерти от голода 0, животное умирает от голода.

### Размножение
Существо может размножаться только при соблюдений следующих условий:
1. в текущей ячейке не достигнут максимальный лимит размножающегося существа
2. в текущей ячейке имеется хотя бы ещё одно существо, такого же класса, что и размножающееся
3. существо живое
4. существо полностью сытое

### Миграция 
Животное может мигрировать в другую ячейку при следующих условиях:
- на дистанцию от 0 (включительно, тогда остаётся в текущей ячейке) до максимальной скорости (включительно) 
(определено в config.properties)
- если в целевой ячейке не достигнут лимит на колличество животных данного класса

### Увеличение голода животного на максимальную величину
В конце каждого цикла происходит увеличение голода животного на максимальную величину. 

Каждый новый цикл животное 
начинает с максимальным показателем голода (равному требуемому количеству пищи, определяемому для каждого животного 
в config.properties).

## Жизненный цикл растения (Plant): на 1 такт симуляции (Creature)

### Размножение.
Аналогично животному, но без условия, что существо должно быть полностью сыто.

## Смерть
### Животное (Animal) может умереть от:
1. голода (когда оно было голодным больше чем наполовину от требуемого дневного колличества пищи в течение больше чем 
заданное колличество циклов в config.properties)
2. другого животного (например, хищника)

### Растение (Plant)
Аналогично животному (Animal), но не может умереть от голода.

## Разложение (если существо не живое)
Каждый цикл (тик), начиная со следующего после смерти существа, если оно не было полностью съедено, уменьщается 
колличество дней до полного разложения (определено в config.properties), после истечения которых,
существо пропадёт и его останки невозможно будет съесть тем, кто мог бы съесть это существо.