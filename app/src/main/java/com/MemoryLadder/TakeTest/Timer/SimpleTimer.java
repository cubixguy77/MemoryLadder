package com.memoryladder.taketest.timer;

public class SimpleTimer extends CountDownTimerPausable {
    private final ITimer.TimerUpdateListener timerUpdateListener;

    private int timeLimitInSeconds;
    private float secondsElapsed;

    public SimpleTimer(int timeLimitInSeconds, float secondsElapsed, ITimer.TimerUpdateListener timerUpdateListener) {
        super((long) (timeLimitInSeconds - secondsElapsed) * 1000, 100);
        this.timerUpdateListener = timerUpdateListener;

        this.timeLimitInSeconds = timeLimitInSeconds;
        this.secondsElapsed = secondsElapsed;

        refreshTimer();
    }

    @Override
    public void onFinish() {
        onTick(0);
        timerUpdateListener.onTimeCountdownComplete();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        secondsElapsed = (timeLimitInSeconds - ((float) millisUntilFinished / 1000));
        refreshTimer();
    }

    private void refreshTimer() {
        timerUpdateListener.onTimeUpdate(getSecondsRemaining(), secondsElapsed);
    }

    private float getSecondsRemaining() {
        return timeLimitInSeconds - secondsElapsed;
    }
}