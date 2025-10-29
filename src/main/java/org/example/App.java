package org.example;

import org.example.model.island.Island;
import org.example.utils.Config;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class App {
    private static Config config = Config.getInstance();
    public static LocalTime startTime = LocalTime.now();

    public static void main(String[] args) {
        Island island = new Island(config.getInt("island.size-x"), config.getInt("island.size-y"));
        island.runSimulation();

        System.out.println("The run lasted: " + ChronoUnit.SECONDS.between(startTime, LocalTime.now()) + " seconds");
    }
}
