package com.memoryladder.taketest.cards;

import android.content.Context;

import com.mastersofmemory.memoryladder.BuildConfig;

public class CardImageProvider {

    public static int getImageResourceId(Context context, PlayingCard card) {
        return context.getResources().getIdentifier(getImageFileNameFor(card), "drawable", BuildConfig.APPLICATION_ID);
    }

    private static String getImageFileNameFor(PlayingCard card) {
        if (card == null)
            return "card_xx";
        else
            return "card_" + getSuitChar(card.suit) + getRankChar(card.rank);
    }

    private static char getSuitChar(int suit) {
        switch (suit) {
            case PlayingCard.DIAMOND: return 'd';
            case PlayingCard.HEART:   return 'h';
            case PlayingCard.CLUB:    return 'c';
            case PlayingCard.SPADE:   return 's';
            default: return 'x';
        }
    }

    private static char getRankChar(int rank) {
        switch (rank) {
            case 10: return 't';
            case 11: return 'j';
            case 12: return 'q';
            case 13: return 'k';
            default: return (char)(rank + '0');
        }
    }
}
