package com.MemoryLadder.Cards;

import android.os.Parcel;
import android.os.Parcelable;

class CardSettings implements Parcelable {

    private int numDecks;
    private int deckSize;
    private int numCardsPerGroup;
    private boolean shuffle;

    private boolean mnemonicsEnabled;

    CardSettings(int numDecks, int deckSize, int numCardsPerGroup, boolean shuffle, boolean isMnemonicsEnabled) {
        this.numDecks = numDecks;
        this.deckSize = deckSize;
        this.numCardsPerGroup = numCardsPerGroup;
        this.shuffle = shuffle;
        this.mnemonicsEnabled = isMnemonicsEnabled;
    }

    int getNumDecks() {
        return numDecks;
    }

    int getDeckSize() {
        return deckSize;
    }

    int getNumCardsPerGroup() {
        return numCardsPerGroup;
    }

    boolean isShuffle() {
        return shuffle;
    }

    boolean isMnemonicsEnabled() {
        return mnemonicsEnabled;
    }

    private CardSettings(Parcel in) {
        numDecks = in.readInt();
        deckSize = in.readInt();
        numCardsPerGroup = in.readInt();
        shuffle = in.readByte() != 0x00;
        mnemonicsEnabled = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numDecks);
        dest.writeInt(deckSize);
        dest.writeInt(numCardsPerGroup);
        dest.writeByte((byte) (shuffle ? 0x01 : 0x00));
        dest.writeByte((byte) (mnemonicsEnabled ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CardSettings> CREATOR = new Parcelable.Creator<CardSettings>() {
        @Override
        public CardSettings createFromParcel(Parcel in) {
            return new CardSettings(in);
        }

        @Override
        public CardSettings[] newArray(int size) {
            return new CardSettings[size];
        }
    };
}