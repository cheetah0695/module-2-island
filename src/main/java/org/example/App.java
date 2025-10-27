package org.example;

import org.example.model.island.Island;
import org.example.utils.Config;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class App {
    private static Config config = Config.getInstance();

    public static void main(String[] args) {
        LocalTime start = LocalTime.now();
        Island island = new Island(config.getInt("island.size.x"), config.getInt("island.size.y"));
        //TODO: for 100x20 deadlocks
        island.runSimulation();

        System.out.println("The run lasted: " + ChronoUnit.SECONDS.between(start, LocalTime.now()) + " seconds");
    }
}
