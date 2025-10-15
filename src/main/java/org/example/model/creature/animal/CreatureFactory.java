package org.example.model.creature.animal;

import org.example.model.creature.Creature;

public interface CreatureFactory {
    Creature createCreature(int x, int y);
}
