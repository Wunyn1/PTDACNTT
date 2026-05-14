package org.example.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

public class PomodoroManager {
    private static PomodoroManager instance;
    private int timeLeft = 25 * 60;
    private Timeline timeline;
    private boolean isRunning = false;
    private Consumer<Integer> onTick;

    private PomodoroManager() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeLeft > 0) {
                timeLeft--;
                if (onTick != null) onTick.accept(timeLeft);
            } else {
                stop();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public static PomodoroManager getInstance() {
        if (instance == null) instance = new PomodoroManager();
        return instance;
    }

    public void start() {
        timeline.play();
        isRunning = true;
    }

    public void pause() {
        timeline.pause();
        isRunning = false;
    }

    public void stop() {
        timeline.stop();
        isRunning = false;
    }

    // Getters & Setters
    public int getTimeLeft() {
        return timeLeft;
    }

    public void setOnTick(Consumer<Integer> onTick) {
        this.onTick = onTick;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
