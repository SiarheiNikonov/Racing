package ru.clevertec;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RacingCar implements Runnable {

    private double currentSpeed = 0;
    private double maxSpeedMpS;
    private double distanceToFinish;
    private AtomicInteger counter;
    private CountDownLatch cdl;
    private long carId;

    public RacingCar(long carId, int maxSpeedKmpH, double distance, CountDownLatch cdl, AtomicInteger counter) {
        this.carId = carId;
        this.maxSpeedMpS = maxSpeedKmpH / 3.6;
        this.distanceToFinish = distance;
        this.cdl = cdl;
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            cdl.countDown();
            cdl.await();
            while (distanceToFinish > 0) {
                Thread.sleep(100);
                changeSpeed();
                distanceToFinish -= currentSpeed / 10;
            }

            distanceToFinish = 0;
            counter.addAndGet(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getDistanceToFinish() {
        return distanceToFinish;
    }

    public long getCarId() {
        return carId;
    }

    private void changeSpeed() {
        double speed = currentSpeed;
        Random random = new Random();
        // between 0-1 m/s (0-3.6 km/h)
        double speedChange = random.nextDouble();

        // if sign == 0 then reduce speed
        int accelerationSign = random.nextInt(7);
        if (accelerationSign == 0) speed -= speedChange;
        else speed += speedChange;

        if (speed > maxSpeedMpS) currentSpeed = maxSpeedMpS;
        else currentSpeed = speed;
    }
}
