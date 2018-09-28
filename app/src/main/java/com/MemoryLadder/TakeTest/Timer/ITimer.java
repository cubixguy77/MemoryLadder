package com.memoryladder.taketest.timer;

public interface ITimer {
    interface View {
        void displayTime(long seconds);
        void hide();
        void show();
    }

    interface TimerUpdateListener {
        void onTimeUpdate(float secondsRemaining, float secondsElapsed);
        void onTimeCountdownComplete();
    }
}
