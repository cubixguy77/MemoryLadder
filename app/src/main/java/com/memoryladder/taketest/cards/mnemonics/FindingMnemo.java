package com.memoryladder.taketest.cards.mnemonics;

import android.content.Context;
import android.content.SharedPreferences;

import com.memoryladder.taketest.cards.PlayingCard;
import com.memoryladder.Utils;

public class FindingMnemo {
    public static String getMnemoFor(PlayingCard card, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Peg_Cards", 0);
        return prefs.getString("prefs_cards_" + card.cardNum, Utils.getCardSuggestions(card.cardNum)[0]);
    }
}
