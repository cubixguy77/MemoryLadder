package com.MemoryLadder.TakeTest;

import com.MemoryLadder.TakeTest.ScorePanel.Score;

public interface GameManager {
    void setGamePhase(GamePhase phase);
    void displayTime(int secondsRemaining);
    void render(GamePhase phase);
    Score getScore();
}
