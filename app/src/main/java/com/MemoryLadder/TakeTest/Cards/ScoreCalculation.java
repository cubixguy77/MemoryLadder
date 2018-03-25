package com.MemoryLadder.TakeTest.Cards;

import com.MemoryLadder.TakeTest.Cards.Deck;
import com.MemoryLadder.TakeTest.ScorePanel.Score;

public class ScoreCalculation {

    /* The scoring works as follows
     * For each deck, you receive 52 points 0 errors, 26 points for 1 error, 0 points for 2+ errors.
     * For the last attempted deck only, if n cards are recalled, you receive n points for 0 errors, ceiling(n/2) points for 1 error, 0 points for 2+ errors.
     */
    public static Score getScore(Deck[] guess, Deck[] answer ) {

        int totalScore = 0;
        int recallCorrect = 0;
        int recallAttempt = 0;

        int lastAttemptedDeck = getLastAttemptedDeck(guess);

        for (int deckNum=0; deckNum<=lastAttemptedDeck; deckNum++) {
            int numCorrect = 0;
            int numBlank = 0;
            int numWrong = 0;
            double deckPoints;

            for (int i=0; i<guess[deckNum].size(); i++) {
                if (guess[deckNum].getCard(i) == null)
                    numBlank++;
                else if (guess[deckNum].getCard(i).equals(answer[deckNum].getCard(i))) {
                    numCorrect++;
                    recallAttempt++;
                }
                else {
                    numWrong++;
                    recallAttempt++;
                }
            }

            recallCorrect += numCorrect;

            if (deckNum == lastAttemptedDeck) {   // final deck only
                if (numWrong == 0)
                    deckPoints = numCorrect;
                else if (numWrong == 1)
                    deckPoints = roundUp((double) (numCorrect) / 2);
                else
                    deckPoints = 0;
            }
            else {
                if (numWrong+numBlank == 0)
                    deckPoints = guess[deckNum].size();
                else if (numWrong+numBlank == 1)
                    deckPoints = roundUp((double) (guess[deckNum].size()) / 2);
                else
                    deckPoints = 0;
            }

            totalScore += deckPoints;
        }

        return new Score((int) ((float) recallCorrect * 100 / recallAttempt), totalScore);
    }

    private static int getLastAttemptedDeck(Deck[] guess) {
        int lastDeckNum = 0;

        for (int deckNum=1; deckNum<guess.length; deckNum++) {
            if (attemptedDeck(guess[deckNum])) {
                lastDeckNum = deckNum;
            }
        }

        return lastDeckNum;
    }

    private static boolean attemptedDeck(Deck deck) {
        for (int i=0; i<deck.size(); i++) {
            if (deck.getCard(i) != null) {
                return true;
            }
        }

        return false;
    }

    private static double roundUp(double d) {
        return (double) ((int) (d + 0.5));
    }
}
