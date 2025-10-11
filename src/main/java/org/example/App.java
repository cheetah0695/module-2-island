package org.example;

import org.example.model.island.Island;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class App {
    public static void main(String[] args) throws InterruptedException {
        LocalTime start = LocalTime.now();
        Island island = new Island(3, 3);
        island.runSimulation();

        System.out.println("The run lasted: " + ChronoUnit.SECONDS.between(start, LocalTime.now()) + " seconds");
    }
}
