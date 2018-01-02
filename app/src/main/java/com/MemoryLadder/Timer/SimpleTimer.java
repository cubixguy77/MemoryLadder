package com.MemoryLadder.Timer;

public class SimpleTimer extends CountDownTimerPausable {
    private final ITimer.TimerUpdateListener timerUpdateListener;

    private int timeLimitInSeconds = 0;
    private float secondsElapsed = 0;
    private float secondsRemaining = 0;

    public SimpleTimer(int timeLimitInSeconds, ITimer.TimerUpdateListener timerUpdateListener) {
        super(timeLimitInSeconds * 1000, 100);
        this.timerUpdateListener = timerUpdateListener;

        this.timeLimitInSeconds = timeLimitInSeconds;
        secondsElapsed = 0;
        secondsRemaining = timeLimitInSeconds;

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
        secondsRemaining = timeLimitInSeconds - secondsElapsed;

        refreshTimer();
    }

    private void refreshTimer() {
        timerUpdateListener.onTimeUpdate(secondsRemaining, secondsElapsed);
    }
}