package com.memoryladder.taketest.randomwords.score;

import com.memoryladder.taketest.ScorePanel.Score;

import java.util.List;

public interface ScoreProvider {
    Score getScore(List<String> memorySheet, List<String> recallSheet, int wordsPerColumn);
}
