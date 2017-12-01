package com.MemoryLadder.Cards;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<PlayingCard> cards;

    /* Generates a deck of size deckSize with optional shuffling */
    public Deck(int deckSize, boolean shuffle) {
        cards = PlayingCardFactory.generateShuffledDeck(deckSize, shuffle);
    }

    /* Generates a deck from an already given list of cards */
    public Deck(List<PlayingCard> cards) {
        this.cards = cards;
    }

    /* Generates a deck of 13 cards, all of the given suit, ordered by Rank (Ace, 2, 3...) */
    public Deck(int suit) {
        List<PlayingCard> cards = new ArrayList<>(13);
        for (int rank=0; rank<13; rank++) {
            cards.add(new PlayingCard(13 * suit + rank));
        }

        this.cards = cards;
    }

    public int size() {
        return cards.size();
    }

    void setCard(int index, PlayingCard card) {
        cards.set(index, card);
    }

    public PlayingCard getCard(int index) {
        return cards.get(index);
    }

    void addCard(PlayingCard card) {
        if (cards.size() == 0)
            cards.add(card);
        else if (card.rank < cards.get(0).rank)
            cards.add(0, card);
        else if (card.rank > cards.get(cards.size()-1).rank)
            cards.add(card);
        else {
            for (int i=1; i<cards.size(); i++) {
                if (card.rank < cards.get(i).rank) {
                    cards.add(i, card);
                    break;
                }
            }
        }
    }

    void removeCard(PlayingCard card) {
        cards.remove(card);
    }

    List<PlayingCard> subList(int start, int end) {
        return cards.subList(start, end);
    }
}
