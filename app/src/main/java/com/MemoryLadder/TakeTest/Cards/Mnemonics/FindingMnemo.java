package com.MemoryLadder.TakeTest.Cards.Mnemonics;

import android.content.Context;
import android.content.SharedPreferences;

import com.MemoryLadder.TakeTest.Cards.PlayingCard;
import com.MemoryLadder.Utils;

public class FindingMnemo {
    public static String getMnemoFor(PlayingCard card, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Peg_Cards", 0);
        return prefs.getString("prefs_cards_" + card.cardNum, Utils.getCardSuggestions(card.cardNum)[0]);
    }
}
