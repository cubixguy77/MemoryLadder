package com.memoryladder.taketest;

import com.memoryladder.taketest.scorepanel.Score;

public interface GameManager {
    void setGamePhase(GamePhase phase);
    void displayTime(int secondsRemaining);
    void render(GamePhase phase);
    Score getScore();
}
