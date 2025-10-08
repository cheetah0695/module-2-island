package org.example.utils;

import org.example.model.island.Island;
import org.example.model.island.IslandCell;

import java.util.ArrayList;

public class IslandCellUtil {
    public static IslandCell getIslandCell(int currentIslandCellX, int currentIslandCellY) {
        ArrayList<IslandCell> islandCells = Island.getIslandCells();
        return islandCells
                .stream()
                .filter(ic -> ic.getX() == currentIslandCellX && ic.getY() == currentIslandCellY)
                .findAny()
                .get();
    }
}
