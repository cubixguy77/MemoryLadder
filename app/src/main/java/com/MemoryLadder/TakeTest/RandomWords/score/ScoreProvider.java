package com.MemoryLadder.TakeTest.RandomWords.score;

import com.MemoryLadder.TakeTest.ScorePanel.Score;

import java.util.List;

public interface ScoreProvider {
    Score getScore(List<String> memorySheet, List<String> recallSheet, int wordsPerColumn);
}
