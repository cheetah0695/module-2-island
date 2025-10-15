package org.example;

import org.example.model.island.Island;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class App {
    public static void main(String[] args) {
        LocalTime start = LocalTime.now();
        Island island = new Island(10, 20);
        //TODO: for 100x20 deadlocks
        island.runSimulation();

        System.out.println("The run lasted: " + ChronoUnit.SECONDS.between(start, LocalTime.now()) + " seconds");
    }
}
