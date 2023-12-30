package com.memoryladder.taketest.cards;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayingCard implements Parcelable {

    /* Index of the card, ranges from 0 - 51 */
    public int cardNum;

    /* Suit ranges from 0 - 3 but should be referenced using the constants provided in this class */
    int suit;

    /* Rank ranges from 1 - 13 */
    int rank;

    static final PlayingCard CORRECT = new PlayingCard(-1);

    static final int SPADE = 0;
    static final int HEART = 1;
    static final int CLUB = 2;
    static final int DIAMOND = 3;

    public static final char TEN =    't';
    public static final char TENc =   'T';
    public static final char JACK =   'j';
    public static final char JACKc =  'J';
    public static final char QUEEN =  'q';
    public static final char QUEENc = 'Q';
    public static final char KING =   'k';
    public static final char KINGc =  'K';
    public static final char ACE =    '1';

    public PlayingCard(int cardNum) {
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

    public static String getCharacter(int val) {
        switch (val) {
            case 1:  return "A";
            case 2:  return "2";
            case 3:  return "3";
            case 4:  return "4";
            case 5:  return "5";
            case 6:  return "6";
            case 7:  return "7";
            case 8:  return "8";
            case 9:  return "9";
            case 10: return "10";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return null;
        }
    }

    protected PlayingCard(Parcel in) {
        cardNum = in.readInt();
        suit = in.readInt();
        rank = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cardNum);
        dest.writeInt(suit);
        dest.writeInt(rank);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlayingCard> CREATOR = new Parcelable.Creator<PlayingCard>() {
        @Override
        public PlayingCard createFromParcel(Parcel in) {
            return new PlayingCard(in);
        }

        @Override
        public PlayingCard[] newArray(int size) {
            return new PlayingCard[size];
        }
    };
}