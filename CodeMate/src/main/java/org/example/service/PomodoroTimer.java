package org.example.service;

import org.example.entity.PomodoroState;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PomodoroTimer {
    private static PomodoroTimer instance;
    private Timer timer;
    private int secondsLeft;
    private PomodoroState currentState = PomodoroState.IDLE; // THÊM MỚI: Để lưu trạng thái hiện tại

    private Consumer<Integer> onTick;
    private Consumer<PomodoroState> onStateChange;
    private Consumer<PomodoroState> onFinish;

    public static PomodoroTimer getInstance() {
        if (instance == null) instance = new PomodoroTimer();
        return instance;
    }

    public void onTick(Consumer<Integer> callback) {
        this.onTick = callback;
    }

    public void onStateChange(Consumer<PomodoroState> callback) {
        this.onStateChange = callback;
    }

    public void onFinish(Consumer<PomodoroState> callback) {
        this.onFinish = callback;
    }

    public void startWork(int seconds) {
        start(seconds, PomodoroState.WORK);
    }

    public void startRest(int seconds) {
        start(seconds, PomodoroState.REST);
    }

    private void start(int seconds, PomodoroState state) {
        stop();
        this.secondsLeft = seconds;
        this.currentState = state;

        if (onStateChange != null) {
            onStateChange.accept(state);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsLeft--;
                if (onTick != null) onTick.accept(secondsLeft);

                if (secondsLeft <= 0) {
                    stop();
                    if (onFinish != null) onFinish.accept(state);
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        this.currentState = PomodoroState.IDLE;
    }


    public int getSecondsLeft() {
        return secondsLeft;
    }

    public PomodoroState getCurrentState() {
        return currentState;
    }
}
