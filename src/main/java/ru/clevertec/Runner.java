package ru.clevertec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Runner {

    public static void main(String[] args) throws InterruptedException, IOException {
        double distance;
        int carsCount;

        System.out.print("Enter distance: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        distance = Double.parseDouble(reader.readLine());
        System.out.print("Enter the number of cars: ");
        carsCount = Integer.parseInt(reader.readLine());

        CountDownLatch cdl = new CountDownLatch(carsCount + 1);
        AtomicInteger finishedCarsCount = new AtomicInteger(0);
        Random random = new Random();
        List<RacingCar> cars = new ArrayList<>();

        for (int i = 0; i < carsCount; i++) {
            //speed between 100 - 300 km/h
            int maxSpeed = random.nextInt(200) + 100;
            RacingCar car = new RacingCar(i, maxSpeed, distance, cdl, finishedCarsCount);
            cars.add(car);
            new Thread(car).start();
        }

        System.out.println("All cars are ready");
        cdl.countDown();

        while (finishedCarsCount.get() != carsCount) {
            Thread.sleep(1000);
            for (RacingCar car : cars) {
                System.out.println(String.format("Car %d, distance to finish: %.1f meters ", car.getCarId(), car.getDistanceToFinish()));
            }
            System.out.println();
        }
        System.out.println("All cars finished!");
    }
}
