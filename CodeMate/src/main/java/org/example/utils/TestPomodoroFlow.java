package org.example.utils;

import org.example.service.PomodoroTimer;

public class TestPomodoroFlow {
    public static void main(String[] args) {
        PomodoroTimer timer = new PomodoroTimer();

        timer.onStateChange(state ->
                System.out.println("STATE = " + state)
        );

        timer.onTick(seconds ->
                System.out.println("TIME LEFT = " + seconds)
        );

        System.out.println("Start WORK 10s");
        timer.startWork(10);

        sleep(12);

        System.out.println("Start REST 5s");
        timer.startRest(5);
    }

    private static void sleep(int s) {
        try {
            Thread.sleep(s * 1000L);
        } catch (InterruptedException ignored) {}
    }
}
