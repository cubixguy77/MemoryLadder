package com.memoryladder.taketest.dates.score;

import com.memoryladder.taketest.dates.models.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

public interface ScoreProvider {
    Score getScore(TestSheet testSheet);
}
