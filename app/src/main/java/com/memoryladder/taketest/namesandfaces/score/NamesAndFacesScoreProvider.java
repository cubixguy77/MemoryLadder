package com.memoryladder.taketest.namesandfaces.score;

import com.memoryladder.taketest.scorepanel.Score;

import java.util.List;

public class NamesAndFacesScoreProvider implements ScoreProvider {

    public Score getScore(List<String> memorySheet, List<String> recallSheet) {
        return new Score(67, 7);
    }
}
