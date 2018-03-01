package com.MemoryLadder.TakeTest.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PlayingCardFactory {

    static List<PlayingCard> generateShuffledDeck(int deckSize, boolean shuffle) {
        List<PlayingCard> deck = new ArrayList<>(52);
        for (int i=0; i<52; i++) {
            deck.add(new PlayingCard(i));
        }

        if (shuffle) {
            Collections.shuffle(deck);
        }

        return deck.subList(0, deckSize);
    }

    static List<PlayingCard> generateEmptyDeck(int deckSize) {
        List<PlayingCard> deck = new ArrayList<>(deckSize);
        for (int i=0; i<deckSize; i++) {
            deck.add(null);
        }

        return deck;
    }
}
