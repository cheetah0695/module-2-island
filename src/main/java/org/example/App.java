package org.example;

import org.example.model.island.Island;

public class App {
    public static void main(String[] args) {
        Island island = new Island(1, 1);
        island.runSimulation();
    }
}
