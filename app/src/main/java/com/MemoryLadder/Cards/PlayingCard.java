package com.MemoryLadder.Cards;

public class PlayingCard {

    /* Index of the card, ranges from 0 - 51 */
    int cardNum;

    /* Suit ranges from 0 - 3 but should be referenced using the constants provided in this class */
    int suit;

    /* Rank ranges from 1 - 13 */
    int rank;

    static final PlayingCard CORRECT = new PlayingCard(-1);

    static final int SPADE = 0;
    static final int HEART = 1;
    static final int CLUB = 2;
    static final int DIAMOND = 3;

    PlayingCard(int cardNum) {
        this.cardNum = cardNum;
        this.suit = cardNum / 13;
        this.rank = cardNum % 13 + 1;
    }

    public boolean equals(Object card) {
        if (card == this)
            return true;

        if (!(card instanceof PlayingCard))
            return false;

        return ((PlayingCard) card).suit == this.suit && ((PlayingCard) card).rank == this.rank;
    }

    public String toString() {
        return this.rank + " " + this.suit;
    }
}
