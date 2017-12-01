package com.MemoryLadder.Cards;

import android.content.Context;
import android.content.SharedPreferences;

import com.MemoryLadder.Utils;

class FindingMnemo {
    static String getMnemoFor(PlayingCard card, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Peg_Cards", 0);
        return prefs.getString("prefs_cards_" + card.cardNum, Utils.getCardSuggestions(card.cardNum)[0]);
    }
}
