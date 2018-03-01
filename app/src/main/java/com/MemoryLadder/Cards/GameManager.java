package com.MemoryLadder.Cards;

import com.MemoryLadder.Cards.ScorePanel.Score;

interface GameManager {
    void setGamePhase(GamePhase phase);
    void displayTime(int secondsRemaining);
    void refreshVisibleComponentsForPhase(GamePhase phase);
    Score getScore();
}
